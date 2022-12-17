package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface LikeDao {
    void deleteByFilmId(long filmId);

    void addByFriends(long filmId, Set<Long> friendList);

    Set<Long> getByFilmId(long filmId);
}
