package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreDao {
   void deleteInFilm(long filmId);

   void updateInFilm(Film film);

   Collection<Film> loadGenres(Collection<Film> films);

   Film loadGenresByFilm(Film film);

   Collection<Genre> getAll();

   Optional<Genre> getById (int genreId);
}