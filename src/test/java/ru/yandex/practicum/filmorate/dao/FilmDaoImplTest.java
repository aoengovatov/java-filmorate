package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql({"/schema.sql"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoImplTest {

    private final FilmDao filmDao;
    private final FilmService filmService;
    private final UserDao userDao;
    private final LikeDao likeDao;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(jdbcTemplate, "film_genres", "film_likes", "films",
                "friends", "users");
    }

    @Test
    public void createFilm() {
        data();
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Mpa mpaTest = new Mpa(1, "G");
        Optional<Film> filmOptional = filmDao.getById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", (long) 1))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Аватар1"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "Описание1"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1995,12,24)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 180))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", mpaTest))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", genres));
    }

    @Test
    public void updateFilm() {
        data();
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Mpa mpa = new Mpa(2, "null");
        Film filmUpdate = new Film(1, "Аватар2", "Описание обновленное",
                LocalDate.of(1996,11,22), 150, 4, mpa, genres);
        filmDao.update(filmUpdate);

        Mpa mpaTest = new Mpa(2, "PG");
        Optional<Film> filmOptional = filmDao.getById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", (long) 1))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Аватар2"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "Описание обновленное"))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1996,11,22)))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 150))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", mpaTest))
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", genres));
    }

    @Test
    public void getFilmsCount(){
        data();
        Mpa mpa = new Mpa(2, "null");
        Set<Genre> genres = new TreeSet<>();
        Film film2 = new Film(2, "Аватар2", "Описание обновленное",
                LocalDate.of(1996,11,22), 150, 4, mpa, genres);
        filmDao.create(film2);
        long films = filmDao.getSize();
        assertEquals(films, 2);
    }

    @Test
    public void getFilms(){
        data();
        Mpa mpa = new Mpa(2, "null");
        Set<Genre> genres = new TreeSet<>();
        Film film2 = new Film(2, "Аватар2", "Описание обновленное",
                LocalDate.of(1996,11,22), 150, 4, mpa, genres);
        filmDao.create(film2);

        Collection<Film> films = filmDao.getAll();
        assertEquals(films.size(), 2);
    }

    @Test
    public void getFilmById(){
        data();
        Optional<Film> filmOptional1 = filmDao.getById(1);
        assertThat(filmOptional1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", (long) 1));
    }

    @Test
    public void addLikes(){
        data();
        Set<Long> filmLikes = likeDao.getAll(1);
        assertEquals(filmLikes.size(), 0);
        Optional<Film> filmOptional = filmDao.getById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0));
        User user1 = new User(1,"mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));
        userDao.create(user1);
        likeDao.addLike(1, user1.getId());

        Set<Long> filmLikes2 = likeDao.getAll(1);
        assertEquals(filmLikes2.size(), 1);
        Optional<Film> filmOptional1 = filmDao.getById(1);
        assertThat(filmOptional1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 1));
    }

    @Test
    public void updateLikes(){
        Mpa mpa = new Mpa(1, "null");
        Set<Genre> genres = new TreeSet<>();
        Film film1 = new Film(1, "Аватар", "Описание",
                LocalDate.of(1995,12,24), 180, 0, mpa, genres);
        filmDao.create(film1);
        Optional<Film> filmOptional = filmDao.getById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0));
        User user1 = new User(1,"mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));
        userDao.create(user1);
        likeDao.addLike(1, user1.getId());
        Optional<Film> filmOptional1 = filmDao.getById(1);
        assertThat(filmOptional1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 1));
    }

    @Test
    public void deleteLikes(){
        Mpa mpa = new Mpa(1, "null");
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Film film1 = new Film(1, "Аватар", "Описание",
                LocalDate.of(1995,12,24), 180, 0, mpa, genres);
        filmDao.create(film1);
        User user1 = new User(1,"mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));
        userDao.create(user1);
        likeDao.addLike(film1.getId(), user1.getId());

        Optional<Film> filmOptional = filmDao.getById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 1));

        likeDao.deleteLike(1, user1.getId());
        Optional<Film> filmOptional1 = filmDao.getById(1);
        assertThat(filmOptional1)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0));
    }

    @Test
    public void getPopular(){
        data();
        Mpa mpa = new Mpa(2, "null");
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Film film2 = new Film(2, "Аватар2", "Описание обновленное",
                LocalDate.of(1996,11,22), 150, 0, mpa, genres);
        filmDao.create(film2);
        User user1 = new User(1,"mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));
        userDao.create(user1);
        likeDao.addLike(film2.getId(), user1.getId());
        List<Film> rateFilms = filmDao.getPopular(2);
        assertEquals(rateFilms.size(), 2);
        Film filmFirstRate = rateFilms.get(0);
        assertThat(filmFirstRate)
                .hasFieldOrPropertyWithValue("rate", 1);
    }

    @Test
    public void getGenres(){
        Collection<Genre> genres = genreDao.getAll();
        assertNotNull(genres);
        assertEquals(genres.size(), 6);
    }

    @Test
    public void getGenreById(){
        Optional<Genre> genreTest = genreDao.getById(2);
        assertThat(genreTest)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 2))
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма"));
    }

    @Test
    public void updateFilmGenre() {
        data();
        Mpa mpa = new Mpa(2, "null");
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Genre genre = new Genre(1, "null");
        genres.add(genre);
        Film filmUpdate = new Film(1, "Аватар2", "Описание обновленное",
                LocalDate.of(1996,11,22), 150, 4, mpa, genres);
        filmService.update(filmUpdate);
        genres.remove(genre);
        genre = new Genre(1, "Комедия");
        genres.add(genre);

        Film film = filmService.getById(Long.valueOf(1));
        assertThat(film)
                .hasFieldOrPropertyWithValue("id", (long) 1)
                .hasFieldOrPropertyWithValue("genres", genres);
    }

    @Test
    public void getMpa(){
        Collection<Mpa> mpa = mpaDao.getAll();
        assertNotNull(mpa);
        assertEquals(mpa.size(), 5);
    }

    @Test
    public void getMpaById(){
        Optional<Mpa> mpaTest = mpaDao.getById(3);
        assertThat(mpaTest)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 3))
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "PG-13"));
    }

    private void data(){
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        Mpa mpa = new Mpa(1, "null");
        Film film1 = new Film(1, "Аватар1", "Описание1",
                LocalDate.of(1995,12,24), 180, 0 , mpa, genres);
        filmDao.create(film1);
    }
}