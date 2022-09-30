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

    @Autowired
    FilmService(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
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
        Set<Long> filmLikes = filmStorage.getLikesById(id);
        filmLikes.add(userId);
        filmStorage.updateLikes(id, filmLikes);
        log.info("Добавление Like фильму с id: " + id + " от пользователя с id: " + userId);
    }

    public void deleteLike(long id, long userId){
        Set<Long> filmLikes = filmStorage.getLikesById(id);
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
        filmStorage.update(film);
        log.info("Обновлен фильм: " + film);
        return film;
    }

    public List<Film> getPopular(Integer count){
        Map<Long, Integer> filmsList =  filmStorage.getLikes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().size()));
        List<Long> films = filmsList.entrySet().stream()
                .sorted(Map.Entry.<Long,Integer>comparingByValue().reversed())
                .limit(count)
                .map(f -> f.getKey())
                .collect(Collectors.toList());
        log.info("Запрос списка популярных фильмов с count: " + count);
        return getFilmsFromList(films);
    }

    private List<Film> getFilmsFromList(List<Long> filmsList){
        return filmStorage.getFilms().stream()
                .filter(f -> filmsList.contains(f.getId()))
                .collect(Collectors.toList());
    }
}