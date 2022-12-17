package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {
    List<User> getFriends(long userId);

    void updateFriends(long id, long friendId, String status);

    void deleteFriend(long id, long userFriend);

    List<User> getCommonFriends(long id, long otherId);
}
