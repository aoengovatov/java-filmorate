package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers(){
        log.info("Количество пользователей: " + userStorage.getSize());
        return userStorage.getUsers();
    }

    public User create(User user){
        userStorage.add(user);
        log.info("Создан пользователь: " + user);
        return user;
    }

    public User update(User user) {
        userStorage.update(user);
        log.info("Обновлен пользователь: " + user);
        return user;
    }

    public List<User> getUserFriends(long userId){
        log.info("Запрос списка друзей пользователя с id: " + userId);
        return getUsersFromList(userStorage.getUserFriends(userId));
    }

    public List<User> getCommonFriends(long id, long otherId){
        Set<Long> userOneFriends = userStorage.getUserFriends(id);
        Set<Long> userTwoFriends = userStorage.getUserFriends(otherId);
        Set<Long> commonFriends = userOneFriends.stream()
                .filter(userTwoFriends::contains)
                .collect(Collectors.toSet());
        log.info("Запрос списка общих друзей пользователей с id: " + id + ", " + otherId);
        return getUsersFromList(commonFriends);
    }

    public User getUserById(Long id){
        log.info("Запрос пользователя с id: " + id);
        return userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь c id: %d не найден", id))
                );
    }

    public void addFriend(long id, long friendId){
        Set<Long> userFriends = userStorage.getUserFriends(id);
        Set<Long> friendFriends = userStorage.getUserFriends(friendId);
        userFriends.add(friendId);
        friendFriends.add(id);
        userStorage.updateFriends(id, userFriends);
        userStorage.updateFriends(friendId, friendFriends);
        log.info("Добавление в друзья пользователей с id: " + id + ", " + friendId);
    }

    public void deleteFriend(long id, long friendId){
        Set<Long> userFriends = userStorage.getUserFriends(id);
        Set<Long> friendFriends = userStorage.getUserFriends(friendId);
        userFriends.remove(friendId);
        friendFriends.remove(id);
        userStorage.updateFriends(id, userFriends);
        userStorage.updateFriends(friendId, friendFriends);
        log.info("Удаление из друзей пользователей с id: " + id + ", " + friendId);
    }

    private List<User> getUsersFromList(Set<Long> userList){
        List<User> users = new ArrayList<>();
        if(userList != null){
            for(Long id : userList){
                users.add(getUserById(id));
            }
        }
        return users;
    }
}