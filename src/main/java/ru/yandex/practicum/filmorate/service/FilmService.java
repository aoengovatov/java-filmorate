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
    private final LikeDao likeDao;

    @Autowired
    FilmService(FilmDao filmDao, UserService userService, LikeDao likeDao){
        this.filmDao = filmDao;
        this.userService = userService;
        this.likeDao = likeDao;
    }

    public Collection<Film> getAll(){
        Collection<Film> films = filmDao.getAll();
        log.info("Количество фильмов: " + films.size());
        return films;
    }

    public Film getById(Long id){
        log.info("Запрос фильма с id: " + id);
        return filmDao.getById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Фильм c id: %d не найден", id))
                );
    }

    public void addLike(long id, long userId){
        filmDao.getById(id);
        Set<Long> filmLikes = likeDao.getByFilmId(id);
        userService.getById(userId);
        filmLikes.add(userId);
        likeDao.addByFriends(id, filmLikes);
        log.info("Добавление Like фильму с id: " + id + " от пользователя с id: " + userId);
    }

    public void deleteLike(long id, long userId){
        filmDao.getById(id);
        Set<Long> filmLikes = likeDao.getByFilmId(id);
        userService.getById(userId);
        filmLikes.remove(userId);
        likeDao.addByFriends(id, filmLikes);
        log.info("Удаление Like у фильма с id: " + id + " от пользователя с id: " + userId);
    }

    public Film create(Film film) {
        log.info("Создан фильм: " + film);
        return filmDao.create(film);
    }

    public Film update(Film film) {
        getById(film.getId());
        log.info("Обновлен фильм: " + film);
        return filmDao.update(film);
    }

    public Collection<Film> getPopular(Integer count){
        return filmDao.getPopular(count);
    }
}