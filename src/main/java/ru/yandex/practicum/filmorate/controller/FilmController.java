package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms(){
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id){
        if(id <= 0){
            log.info("Запрос фильма с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if(film.getId() <= 0){
            log.info("Обновление фильма с неверным id: " + film.getId());
            throw new IncorrectParameterException("id");
        }
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId){
        if(id <= 0){
            log.info("Like фильму с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        if(userId <= 0){
            log.info("Like от пользователя с неверным id: " + userId);
            throw new IncorrectParameterException("userId");
        }
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId){
        if(id <= 0){
            log.info("Удаление Like у фильма с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        if(userId <= 0){
            log.info("Удаление Like от пользователя с неверным id: " + userId);
            throw new IncorrectParameterException("userId");
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10", required = false) Integer count){
        if (count <= 0) {
            log.info("Запрос списка популярных фильмов с неверным count: " + count);
            throw new IncorrectParameterException("count");
        }
        return filmService.getPopular(count);
    }
}