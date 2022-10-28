package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {


    private final UserStorage userStorage;

    @Autowired
    UserService(@Qualifier("userDbStorage") UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers(){
        log.info("Количество пользователей: " + userStorage.getSize());
        return userStorage.getUsers();
    }

    public User create(User user){
        user = userStorage.add(user);
        log.info("Создан пользователь: " + user);
        return user;
    }

    public User update(User user) {
        getUserById(user.getId());
        userStorage.update(user);
        log.info("Обновлен пользователь: " + user);
        return user;
    }

    public List<User> getUserFriends(long userId){
        getUserById(userId);
        log.info("Запрос списка друзей пользователя с id: " + userId);
        return userStorage.getUserFriends(userId).stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId){
        getUserById(id);
        getUserById(otherId);
        log.info("Запрос списка общих друзей пользователей с id: " + id + ", " + otherId);
        Set<Long> userOneFriends = userStorage.getUserFriends(id);
        Set<Long> userTwoFriends = userStorage.getUserFriends(otherId);
        return userOneFriends.stream()
                .filter(userTwoFriends::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User getUserById(Long id){
        log.info("Запрос пользователя с id: " + id);
        return userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь c id: %d не найден", id))
                );
    }

    public void addFriend(long id, long friendId){
        getUserById(id);
        getUserById(friendId);
        Set<Long> userFriends = userStorage.getUserFriends(id);
        userFriends.add(friendId);
        userStorage.updateFriends(id, userFriends);
        log.info("Добавление в друзья user с id: " + id + " друга с id: " + friendId);
    }

    public void deleteFriend(long id, long friendId){
        getUserById(id);
        getUserById(friendId);
        Set<Long> userFriends = userStorage.getUserFriends(id);
        if(userFriends.contains(friendId)){
            userStorage.deleteFriend(id, friendId);
            log.info("Удаление из друзей user с id: " + id + " друга с id: " + friendId);
        }
    }
}