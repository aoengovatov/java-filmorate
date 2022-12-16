package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao {
    Optional<User> getById(long id);

    User create(User user);

    void update(User user);

    void delete(User user);

    List<User> getAll();

    //Set<Long> getUserFriends(long userId);

    //void updateFriends(long id, long friendId, String status);

    //void deleteFriend(long id, long userFriend);

    long getSize();
}