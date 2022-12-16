package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage{

    private final FilmDao filmDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;

    private final MpaDao mpaDao;

    @Autowired
    public FilmDbStorage(FilmDao filmDao, LikeDao likeDao, GenreDao genreDao, MpaDao mpaDao) {
        this.filmDao = filmDao;
        this.likeDao = likeDao;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
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
        return filmDao.getAll();
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        return filmDao.getById(filmId);
    }

    @Override
    public Set<Long> getLikesById(long id) {
        return likeDao.getByFilmId(id);
    }

    @Override
    public void updateLikes(long id, Set<Long> friendList) {
        likeDao.addByFriendList(id, friendList);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return filmDao.getPopular(count);
    }

    @Override
    public Collection<Genre> getGenres() {
        return genreDao.getAll();
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return genreDao.getById(id);
    }

    @Override
    public Collection<Mpa> getMpa() {
        return mpaDao.getAll();
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        return mpaDao.getById(id);
    }
}