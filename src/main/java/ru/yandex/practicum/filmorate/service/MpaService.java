package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@Slf4j
public class MpaService {

    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao){
        this.mpaDao = mpaDao;
    }

    public Collection<Mpa> getAll() {
        return mpaDao.getAll();
    }

    public Mpa getById(int id) {
        log.info("Запрос Mpa с id: " + id);
        return mpaDao.getById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Mpa c id: %d не найден", id))
                );
    }
}