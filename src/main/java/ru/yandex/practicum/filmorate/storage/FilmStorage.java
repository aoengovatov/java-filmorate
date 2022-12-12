package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    void delete(Film film);

    long getSize();

    Collection<Film> getFilms();

    Optional<Film> getFilmById(long filmId);

    Map<Long, Set<Long>> getLikes();

    Set<Long> getLikesById(long id);

    void updateLikes(long id, Set<Long> friendList, int rate);

    List<Film> getPopular(Integer count);

    Collection<Genre> getGenres();

    Optional<Genre> getGenreById(int id);

    Collection<Mpa> getMpa();

    Optional<Mpa> getMpaById(int id);
}