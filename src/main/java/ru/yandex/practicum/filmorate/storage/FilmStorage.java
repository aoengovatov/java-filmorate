package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    void add(Film film);

    void update(Film film);

    void delete(Film film);

    Integer getSize();

    Collection<Film> getFilms();

    Map<Long, Set<Long>> getLikes();

    Set<Long> getLikesById(long id);

    void updateLikes(long id, Set<Long> friendList);
}