package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface LikeDao {
    void deleteByFilmId(long filmId);

    void addByFriends(long filmId, Set<Long> friendList);

    Set<Long> getByFilmId(long filmId);
}
