package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage{

    private final UserDao userDao;

    @Autowired
    public UserDbStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User add(User user) {
        return userDao.create(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public Collection<User> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public Set<Long> getUserFriends(long userId) {
        return userDao.getUserFriends(userId);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public void updateFriends(long id, Set<Long> friends) {
        Set<Long> oldFriends = userDao.getUserFriends(id);
        List<Long> newFriends = friends.stream()
                .filter(element -> !oldFriends.contains(element))
                .collect(Collectors.toList());
        if(newFriends.size() == 1){
            userDao.updateFriends(id, newFriends.get(0), "true");
        }
    }

    @Override
    public void deleteFriend(long id, long userFriend) {
        userDao.deleteFriend(id, userFriend);
    }
}