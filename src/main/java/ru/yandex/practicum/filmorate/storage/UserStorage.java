package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    User add(User user);

    void update(User user);

    void delete(User user);

    long getSize();

    Collection<User> getUsers();

    Set<Long> getUserFriends(long userId);

    Optional<User> getUserById(long userId);

    void deleteFriend(long id, long userFriend);

    void updateFriends(long id, Set<Long> userFriends);
}