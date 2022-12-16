package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FriendDao {
    Set<Long> getFriends(long userId);

    void updateFriends(long id, long friendId, String status);

    void deleteFriend(long id, long userFriend);
}
