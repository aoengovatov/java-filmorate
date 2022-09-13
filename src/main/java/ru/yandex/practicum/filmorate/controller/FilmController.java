package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    @GetMapping
    public Collection<Film> getFilms(){
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ParseException {
        if(film.getReleaseDate().before(formatter.parse("1895-12-28"))){
            throw new ValidationException("Ошибка. Дата фильма не должна быть раньше Дня рождения кино");
        }
        int id = generateId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ParseException {
        if(film.getReleaseDate().before(formatter.parse("1895-12-28"))){
            throw new ValidationException("Ошибка. Дата фильма не должна быть раньше Дня рождения кино");
        }
        if(films.containsKey(film.getId())){
            films.put(id, film);
        } else {
            throw new ValidationException("Ошибка. Нет фильма с id: " + film.getId());
        }
        return film;
    }

    private int generateId(){
        return ++id;
    }
}