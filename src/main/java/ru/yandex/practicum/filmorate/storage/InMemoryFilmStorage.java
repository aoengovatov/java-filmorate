package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Long, Film> films;
    private final Map<Long, Set<Long>> likes;
    private long id = 0;

    @Override
    public Film add(Film film) {
        long id = generateId();
        film.setId(id);
        films.put(id, film);
        Set<Long> LikesList = new HashSet<>();
        likes.put(id, LikesList);
        return null;
    }

    @Override
    public Film update(Film film) {
        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        }
        return film;
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
        return Optional.ofNullable(films.get(filmId));
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
    public void updateLikes(long id, Set<Long> friendList, int rate) {
        likes.computeIfAbsent(id, u -> friendList);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return null;
    }

    @Override
    public Collection<Genre> getGenres() {
        return null;
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return null;
    }

    @Override
    public Collection<Mpa> getMpa() {
        return null;
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        return Optional.empty();
    }

    private long generateId(){
        return ++id;
    }
}