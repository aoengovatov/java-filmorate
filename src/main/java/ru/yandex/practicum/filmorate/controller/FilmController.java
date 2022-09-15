package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895,12,28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<Film> getFilms(){
        log.info("Количество фильмов: " + films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        dateValid(new java.sql.Date(film.getReleaseDate().getTime()).toLocalDate());
        int id = generateId();
        film.setId(id);
        films.put(id, film);
        log.info("Создан фильм: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        dateValid(new java.sql.Date(film.getReleaseDate().getTime()).toLocalDate());
        if(films.containsKey(film.getId())){
            films.put(id, film);
        } else {
            log.warn("Нет фильма с id: " + film.getId());
            throw new ValidationException("Ошибка. Нет фильма с id: " + film.getId());
        }
        log.info("Обновлен фильм: " + film);
        return film;
    }

    private int generateId(){
        return ++id;
    }

    private static void dateValid (LocalDate date){
        if(date.isBefore(FILM_BIRTHDAY)){
            log.warn("Ошибка обновления фильма с датой:" + date);
            throw new ValidationException("Ошибка. Дата фильма не должна быть раньше Дня рождения кино");
        }
    }
}