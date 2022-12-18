package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface LikeDao {
    void deleteLike(long filmId, long userId);

    void addLike(long filmId, long userId);

    Set<Long> getAll(long filmId);
}
