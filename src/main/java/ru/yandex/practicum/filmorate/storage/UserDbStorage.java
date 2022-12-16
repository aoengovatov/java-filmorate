package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
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
    private final FriendDao friendDao;

    @Autowired
    public UserDbStorage(UserDao userDao, FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
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
        return userDao.getSize();
    }

    @Override
    public Collection<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public Set<Long> getFriends(long userId) {
        return friendDao.getFriends(userId);
    }

    @Override
    public Optional<User> getById(long userId) {
        return userDao.getById(userId);
    }

    @Override
    public void updateFriends(long id, Set<Long> friends) {
        Set<Long> oldFriends = friendDao.getFriends(id);
        List<Long> newFriends = friends.stream()
                .filter(element -> !oldFriends.contains(element))
                .collect(Collectors.toList());
        if(newFriends.size() == 1){
            friendDao.updateFriends(id, newFriends.get(0), "true");
        }
    }

    @Override
    public void deleteFriend(long id, long userFriend) {
        friendDao.deleteFriend(id, userFriend);
    }
}