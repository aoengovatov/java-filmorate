package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao){
        this.genreDao = genreDao;
    }

    public Collection<Genre> getAll() {
        return genreDao.getAll();
    }

    public Genre getById(int id) {
        log.info("Запрос жарна с id: " + id);
        return genreDao.getById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Жанр c id: %d не найден", id))
                );
    }

    public Film loadGenresByFilm(Film film) {
        return genreDao.loadGenresByFilm(film);
    }

    public void loadGenres(List<Film> films) {
        genreDao.loadGenres(films);
    }

    public void deleteInFilm(long id) {
        genreDao.deleteInFilm(id);
    }

    public void updateInFilm(Film film) {
        genreDao.updateInFilm(film);
    }
}