package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private int id = 0;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895,12,28);

    @Autowired
    FilmService(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms(){
        log.info("Количество фильмов: " + filmStorage.getSize());
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id){
        log.info("Запрос фильма с id: " + id);
        return filmStorage.getFilms().stream()
                .filter(f -> id.equals(f.getId()))
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Фильм c id: %d не найден", id))
                );
    }

    public void addLike(long id, long userId){
        Set<Long> filmLikes = new HashSet<>();
        if(filmStorage.getLikesById(id) != null){
            filmLikes = filmStorage.getLikesById(id);
        }
        filmLikes.add(userId);
        filmStorage.updateLikes(id, filmLikes);
        log.info("Добавление Like фильму с id: " + id + " от пользователя с id: " + userId);
    }

    public void deleteLike(long id, long userId){
        Set<Long> filmLikes = new HashSet<>();
        if(filmStorage.getLikesById(id) != null){
            filmLikes = filmStorage.getLikesById(id);
            filmLikes.remove(userId);
        }
        filmStorage.updateLikes(id, filmLikes);
        log.info("Удаление Like у фильма с id: " + id + " от пользователя с id: " + userId);
    }

    public Film create(Film film) {
        film = dateValid(film);
        int id = generateId();
        film.setId(id);
        filmStorage.add(film);
        log.info("Создан фильм: " + film);
        return film;
    }

    public Film update(Film film) {
        film = dateValid(film);
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

    private static Film dateValid (Film film){
        if(film.getReleaseDate().isBefore(FILM_BIRTHDAY)){
            log.warn("Ошибка обновления фильма с датой:" + film.getReleaseDate());
            throw new ValidationException("Ошибка. Дата фильма не должна быть раньше Дня рождения кино");
        }
        return film;
    }

    private List<Film> getFilmsFromList(List<Long> filmsList){
        List<Film> films = new ArrayList<>();
        if(filmsList != null){
            for(Long id : filmsList){
                films.add(getFilmById(id.intValue()));
            }
        }
        return films;
    }

    private int generateId(){
        return ++id;
    }
}