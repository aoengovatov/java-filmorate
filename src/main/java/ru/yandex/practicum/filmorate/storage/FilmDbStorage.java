package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage{

    private final FilmDao filmDao;

    @Autowired
    public FilmDbStorage(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Override
    public Film add(Film film) {
        return filmDao.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmDao.update(film);
    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public long getSize() {
        return filmDao.getSize();
    }

    @Override
    public Collection<Film> getFilms() {
        return filmDao.getFilms();
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        return filmDao.getFilmById(filmId);
    }

    @Override
    public Map<Long, Set<Long>> getLikes() {
        return null;
    }

    @Override
    public Set<Long> getLikesById(long id) {
        return filmDao.getLikesById(id);
    }

    @Override
    public void updateLikes(long id, Set<Long> friendList, int rate) {
        filmDao.updateLikes(id, friendList, rate);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return filmDao.getPopular(count);
    }

    @Override
    public Collection<Genre> getGenres() {
        return filmDao.getGenres();
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return filmDao.getGenreById(id);
    }

    @Override
    public Collection<Mpa> getMpa() {
        return filmDao.getMpa();
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        return filmDao.getMpaById(id);
    }
}