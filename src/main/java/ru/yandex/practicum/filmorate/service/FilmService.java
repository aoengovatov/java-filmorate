package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.*;

@Service
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    private final UserService userService;
    private final GenreService genreService;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(FilmDao filmDao, UserService userService, LikeDao likeDao,
                GenreService genreService){
        this.filmDao = filmDao;
        this.userService = userService;
        this.likeDao = likeDao;
        this.genreService = genreService;
    }

    public List<Film> getAll(){
        List<Film> films = filmDao.getAll();
        genreService.loadGenres(films);
        log.info("Количество фильмов: " + films.size());
        return films;
    }

    public Film getById(Long id){
        log.info("Запрос фильма с id: " + id);
        Optional<Film> film = filmDao.getById(id);
        if(film.isPresent()){
            genreService.loadGenresByFilm(film.get());
        }
        return film.orElseThrow(() -> new FilmNotFoundException(String.format("Фильм c id: %d не найден", id))
                );
    }

    public void addLike(long id, long userId){
        filmDao.getById(id);
        userService.getById(userId);
        likeDao.addLike(id, userId);
        log.info("Добавление Like фильму с id: " + id + " от пользователя с id: " + userId);
    }

    public void deleteLike(long id, long userId){
        filmDao.getById(id);
        userService.getById(userId);
        likeDao.deleteLike(id, userId);
        log.info("Удаление Like у фильма с id: " + id + " от пользователя с id: " + userId);
    }

    public Film create(Film film) {
        filmDao.create(film);
        genreService.updateInFilm(film);
        genreService.loadGenresByFilm(film);
        log.info("Создан фильм: " + film);
        return film;
    }

    public Film update(Film film) {
        getById(film.getId());
        filmDao.update(film);
        log.info("Обновлен фильм: " + film);
        genreService.deleteInFilm(film.getId());
        genreService.updateInFilm(film);
        genreService.loadGenresByFilm(film);
        return film;
    }

    public List<Film> getPopular(Integer count){
        List<Film> films = filmDao.getPopular(count);
        genreService.loadGenres(films);
        return films;
    }
}