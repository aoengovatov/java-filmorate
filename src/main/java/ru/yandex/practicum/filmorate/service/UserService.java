package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final FriendDao friendDao;

    @Autowired
    UserService(UserDao userDao, FriendDao friendDao){
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    public Collection<User> getAll(){
        log.info("Количество пользователей: " + userDao.getSize());
        return userDao.getAll();
    }

    public User create(User user){
        user = userDao.create(user);
        log.info("Создан пользователь: " + user);
        return user;
    }

    public User update(User user) {
        getById(user.getId());
        userDao.update(user);
        log.info("Обновлен пользователь: " + user);
        return user;
    }

    public List<User> getFriends(long userId){
        getById(userId);
        log.info("Запрос списка друзей пользователя с id: " + userId);
        return friendDao.getFriends(userId).stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId){
        getById(id);
        getById(otherId);
        log.info("Запрос списка общих друзей пользователей с id: " + id + ", " + otherId);
        return friendDao.getCommonFriends(id, otherId);
    }

    public User getById(Long id){
        log.info("Запрос пользователя с id: " + id);
        return userDao.getById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь c id: %d не найден", id))
                );
    }

    public void addFriend(long id, long friendId){
        getById(id);
        getById(friendId);
        Set<Long> userFriends = friendDao.getFriends(id);
        userFriends.add(friendId);
        Set<Long> oldFriends = friendDao.getFriends(id);
        List<Long> newFriends = userFriends.stream()
                .filter(element -> !oldFriends.contains(element))
                .collect(Collectors.toList());
        if(newFriends.size() == 1){
            friendDao.updateFriends(id, newFriends.get(0), "true");
        }
        log.info("Добавление в друзья user с id: " + id + " друга с id: " + friendId);
    }

    public void deleteFriend(long id, long friendId){
        getById(id);
        getById(friendId);
        Set<Long> userFriends = friendDao.getFriends(id);
        if(userFriends.contains(friendId)){
            friendDao.deleteFriend(id, friendId);
            log.info("Удаление из друзей user с id: " + id + " друга с id: " + friendId);
        }
    }
}