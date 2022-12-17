package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FriendDao {
    Set<Long> getFriends(long userId);

    void updateFriends(long id, long friendId, String status);

    void deleteFriend(long id, long userFriend);

    List<User> getCommonFriends(long id, long otherId);
}
