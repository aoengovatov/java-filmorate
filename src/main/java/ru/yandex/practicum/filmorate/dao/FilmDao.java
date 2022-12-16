package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmDao {

    Film create(Film film);

    long getSize();

    Collection<Film> getAll();

    Optional<Film> getById(long filmId);

    Film update(Film film);

    List<Film> getPopular(Integer count);

    //Collection<Genre> getGenres();

    //Optional<Genre> getGenreById(int id);

    //Collection<Mpa> getMpa();

    //Optional<Mpa> getMpaById(int id);
}