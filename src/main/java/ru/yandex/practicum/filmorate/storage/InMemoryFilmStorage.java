package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Long, Film> films;
    private final Map<Long, Set<Long>> likes;
    private long id = 0;

    @Override
    public void add(Film film) {
        long id = generateId();
        film.setId(id);
        films.put(id, film);
        Set<Long> LikesList = new HashSet<>();
        likes.put(id, LikesList);
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
    public long getSize() {
        return films.size();
    }

    @Override
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Optional<Film> getFilmById(long filmId){
        return Optional.of(films.get(filmId));
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
        likes.computeIfAbsent(id, u -> friendList);
    }

    private long generateId(){
        return ++id;
    }
}