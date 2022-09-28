package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private int id = 0;

    @Autowired
    UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers(){
        log.info("Количество пользователей: " + userStorage.getSize());
        return userStorage.getUsers();
    }

    public User create( User user){
        int id = generateId();
        user.setId(id);
        user = validNameAndLogin(user);
        userStorage.add(user);
        log.info("Создан пользователь: " + user);
        return user;
    }

    public User update(User user) {
        userStorage.add(validNameAndLogin(user));
        log.info("Обновлен пользователь: " + user);
        return user;
    }

    public List<User> getUserFriends(Integer userId){
        return getUsersFromList(userStorage.getUserFriends(userId));
    }

    public List<User> getCommonFriends(Integer id, Integer otherId){
        Set<Long> userOneFriends = userStorage.getUserFriends(id);
        Set<Long> userTwoFriends = userStorage.getUserFriends(otherId);
        List<Long> allFriends = new ArrayList<>();
        if(userOneFriends != null && userTwoFriends != null){
            allFriends = Stream.concat(userOneFriends.stream(),
                    userTwoFriends.stream()).collect(Collectors.toList());
        }
        Set<Long> commonFriends = findDuble(allFriends);
        return getUsersFromList(commonFriends);
    }

    public User getUserById(Integer id){
        return userStorage.getUsers().stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь c id: %d не найден", id))
                );
    }

    public void addFriend(long id, long friendId){
        Set<Long> userFriends = new HashSet<>();
        Set<Long> friendFriends = new HashSet<>();
        if(userStorage.getUserFriends(id) != null){
            userFriends = userStorage.getUserFriends(id);
        }
        if(userStorage.getUserFriends(friendId) != null){
            friendFriends = userStorage.getUserFriends(friendId);
        }
        userFriends.add(friendId);
        friendFriends.add(id);
        userStorage.updateFriends(id, userFriends);
        userStorage.updateFriends(friendId, friendFriends);
    }

    public void deleteFriend(long id, long friendId){
        Set<Long> userFriends = new HashSet<>();
        Set<Long> friendFriends = new HashSet<>();
        if(userStorage.getUserFriends(id) != null){
            userFriends = userStorage.getUserFriends(id);
            userFriends.remove(friendId);
        }
        if(userStorage.getUserFriends(friendId) != null){
            friendFriends = userStorage.getUserFriends(friendId);
            friendFriends.remove(id);
        }
        userStorage.updateFriends(id, userFriends);
        userStorage.updateFriends(friendId, friendFriends);
    }

    private int generateId(){
        return ++id;
    }

    private static User validNameAndLogin(User user) {
        if(user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        if(user.getLogin().contains(" ")){
            throw new ValidationException("Ошибка. Логин пользователя не должно содержать пробелы");
        }
        return user;
    }

    private static <T> Set<T> findDuble(Collection<T> collection) {
        Set<T> elements = new HashSet<>();
        return collection.stream()
                .filter(e -> !elements.add(e))
                .collect(Collectors.toSet());
    }

    private List<User> getUsersFromList(Set<Long> userList){
        List<User> users = new ArrayList<>();
        if(userList != null){
            for(Long id : userList){
                users.add(getUserById(id.intValue()));
            }
        }
        return users;
    }
}