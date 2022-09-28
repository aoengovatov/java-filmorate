package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

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
        //TODO проверки на пустой id, отрицательный id
        filmStorage.update(film);
        log.info("Обновлен фильм: " + film);
        return film;
    }

    private static Film dateValid (Film film){
        if(film.getReleaseDate().isBefore(FILM_BIRTHDAY)){
            log.warn("Ошибка обновления фильма с датой:" + film.getReleaseDate());
            throw new ValidationException("Ошибка. Дата фильма не должна быть раньше Дня рождения кино");
        }
        return film;
    }

    private int generateId(){
        return ++id;
    }
}