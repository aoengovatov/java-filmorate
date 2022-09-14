package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final Date filmBirthday = formatter.parse("1895-12-28");
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    public FilmController() throws ParseException {
    }

    @GetMapping
    public Collection<Film> getFilms(){
        log.info("Количество фильмов: " + films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if(film.getReleaseDate().before(filmBirthday)){
            log.warn("Ошибка создания фильма с датой:" + film.getReleaseDate());
            throw new ValidationException("Ошибка. Дата фильма не должна быть раньше Дня рождения кино");
        }
        int id = generateId();
        film.setId(id);
        films.put(id, film);
        log.info("Создан фильм: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if(film.getReleaseDate().before(filmBirthday)){
            log.warn("Ошибка обновления фильма с датой:" + film.getReleaseDate());
            throw new ValidationException("Ошибка. Дата фильма не должна быть раньше Дня рождения кино");
        }
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
}