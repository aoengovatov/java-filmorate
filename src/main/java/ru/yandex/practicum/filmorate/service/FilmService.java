package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    FilmService(FilmStorage filmStorage, UserService userService){
        this.filmStorage = filmStorage;
        this.userService = userService;
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
        filmStorage.updateLikes(id, filmLikes);
        log.info("Добавление Like фильму с id: " + id + " от пользователя с id: " + userId);
    }

    public void deleteLike(long id, long userId){
        filmStorage.getFilmById(id);
        Set<Long> filmLikes = filmStorage.getLikesById(id);
        userService.getUserById(userId);
        filmLikes.remove(userId);
        filmStorage.updateLikes(id, filmLikes);
        log.info("Удаление Like у фильма с id: " + id + " от пользователя с id: " + userId);
    }

    public Film create(Film film) {
        filmStorage.add(film);
        log.info("Создан фильм: " + film);
        return film;
    }

    public Film update(Film film) {
        getFilmById(film.getId());
        filmStorage.update(film);
        log.info("Обновлен фильм: " + film);
        return film;
    }

    public List<Film> getPopular(Integer count){
        return filmStorage.getFilms().stream().sorted((o1, o2) ->
                        filmStorage.getLikesById(o2.getId()).size() - filmStorage.getLikesById(o1.getId()).size())
                .limit(count)
                .collect(Collectors.toList());
    }
}