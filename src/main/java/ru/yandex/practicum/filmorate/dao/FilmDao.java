package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    Film create(Film film);

    long getSize();

    List<Film> getAll();

    Optional<Film> getById(long filmId);

    Film update(Film film);

    List<Film> getPopular(Integer count);
}