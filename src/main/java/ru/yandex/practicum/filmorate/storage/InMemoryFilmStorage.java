package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films;
    private final Map<Long, Set<Long>> likes;

    @Override
    public void add(Film film) {
        films.put(film.getId(), film);
        Set<Long> LikesList = new HashSet<>();
        likes.put((long) film.getId(), LikesList);
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

    @Override
    public Map<Long, Set<Long>> getLikes() {
        return likes;
    }

    @Override
    public Set<Long> getLikesById(long id) {
        return likes.get(id);
    }

    @Override
    public void updateLikes(long id, Set<Long> friendList) {
        likes.put(id, friendList);
    }
}