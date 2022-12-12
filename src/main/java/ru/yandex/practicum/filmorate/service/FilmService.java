package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService){
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Genre getGenreById(int id) {
        log.info("Запрос жарна с id: " + id);
        return filmStorage.getGenreById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Жанр c id: %d не найден", id))
                );
    }

    public Collection<Film> getFilms(){
        log.info("Количество фильмов: " + filmStorage.getSize());
        return filmStorage.getFilms();
    }

    public Film getFilmById(Long id){
        log.info("Запрос фильма с id: " + id);
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Фильм c id: %d не найден", id))
                );
    }

    public void addLike(long id, long userId){
        filmStorage.getFilmById(id);
        Set<Long> filmLikes = filmStorage.getLikesById(id);
        userService.getUserById(userId);
        filmLikes.add(userId);
        filmStorage.updateLikes(id, filmLikes, 1);
        log.info("Добавление Like фильму с id: " + id + " от пользователя с id: " + userId);
    }

    public void deleteLike(long id, long userId){
        filmStorage.getFilmById(id);
        Set<Long> filmLikes = filmStorage.getLikesById(id);
        userService.getUserById(userId);
        filmLikes.remove(userId);
        filmStorage.updateLikes(id, filmLikes, -1);
        log.info("Удаление Like у фильма с id: " + id + " от пользователя с id: " + userId);
    }

    public Film create(Film film) {
        log.info("Создан фильм: " + film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        getFilmById(film.getId());
        log.info("Обновлен фильм: " + film);
        return filmStorage.update(film);
    }

    public List<Film> getPopular(Integer count){
        return filmStorage.getPopular(count);
    }

    public Collection<Mpa> getMpa() {
        return filmStorage.getMpa();
    }

    public Mpa getMpaById(int id) {
        log.info("Запрос Mpa с id: " + id);
        return filmStorage.getMpaById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Mpa c id: %d не найден", id))
                );
    }
}