package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films = null;

    @Override
    public void add(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        }
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    @Override
    public Integer getSize() {
        return films.size();
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }
}