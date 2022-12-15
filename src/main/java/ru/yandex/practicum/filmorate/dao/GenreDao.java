package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

public interface GenreDao {
   void deleteInFilm(long filmId);

   void updateInFilm(Film film);
}
