package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao{

    private JdbcTemplate jdbcTemplate;

    private GenreDao genreDao;
    private LikeDao likeDao;
    public FilmDaoImpl(JdbcTemplate jdbcTemplate, GenreDao genreDao, LikeDao likeDao){
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa, rate) " +
                "values (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            stmt.setInt(6, 0);
            return stmt;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        film.setId(id);

        if(!film.getGenres().isEmpty()){
            for (Genre genre : film.getGenres()){
                String sql = "insert into film_genres (film_id, genre_id) " +
                        "values (?,?)";
                jdbcTemplate.update(sql, id, genre.getId());
            }
        }
        film.setGenres(getGenresByFilm(id));
        return film;
    }

    @Override
    public long getSize() {
        String sqlQuery = "select count(*) from films";
        return jdbcTemplate.queryForObject(
                sqlQuery, Long.class);
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "select * from films as f join mpa as m on f.mpa = m.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        String sql1 = "select * from films as f join mpa as m on f.mpa = m.mpa_id and f.id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql1, filmId);
        if(filmRows.next()) {
            Film film = jdbcTemplate.queryForObject(sql1, (rs, rowNum) -> mapRowToFilm(rs, rowNum), filmId);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с id: {} не найден.", filmId);
            return Optional.empty();
        }
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("update films set name = ?, description = ?, release_date = ?, " +
                        "duration = ?, rate = ?, mpa = ? where id = ?", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), 0, film.getMpa().getId(), film.getId());
        genreDao.deleteInFilm(film.getId());
        genreDao.updateInFilm(film);
        film.setGenres(getGenresByFilm(film.getId()));
        return film;
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sqlQuery = "select * from films as f join mpa as m on f.mpa = m.mpa_id " +
                "order by rate desc limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public Collection<Genre> getGenres() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", genreId);
        if(genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name"));

            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());

            return Optional.of(genre);
        } else {
            log.info("Жанр с id: {} не найден.", genreId);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Mpa> getMpa() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> getMpaById(int mpaId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from mpa where mpa_id = ?", mpaId);
        if(genreRows.next()) {
            Mpa mpa = new Mpa(
                    genreRows.getInt("mpa_id"),
                    genreRows.getString("mpa_name"));

            log.info("Найден mpa: {} {}", mpa.getId(), mpa.getName());

            return Optional.of(mpa);
        } else {
            log.info("Mpa с id: {} не найден.", mpaId);
            return Optional.empty();
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int mpaId = resultSet.getInt("mpa_id");
        String mpaName = resultSet.getString("mpa_name");;
        Mpa mpa = new Mpa(mpaId, mpaName);

        Film film = Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpa)
                .rate(resultSet.getInt("rate"))
                .build();
        film.setGenres(getGenresByFilm(film.getId()));
        return film;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }

    private Set<Genre> getGenresByFilm(long filmId) {
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        String sql1 = "select * from genre as g " +
                "join film_genres as f on f.genre_id = g.id where f.film_id = ?";
        List<Genre> genresByFilm = jdbcTemplate.query(sql1, this::mapRowToGenre, filmId);
        genres.addAll(genresByFilm);
        return genres;
    }
}