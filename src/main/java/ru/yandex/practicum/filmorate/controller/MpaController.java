package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Mpa> getMpa(){
        return filmService.getMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id){
        if(id <= 0){
            log.info("Запрос рейтинга с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        return filmService.getMpaById(id);
    }
}