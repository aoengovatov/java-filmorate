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
    public FilmDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
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
        return film;
    }

    @Override
    public long getSize() {
        String sqlQuery = "select count(*) from films";
        return jdbcTemplate.queryForObject(
                sqlQuery, Long.class);
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select * from films as f join mpa as m on f.mpa = m.mpa_id";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        return films;
    }

    @Override
    public Optional<Film> getById(long filmId) {
        String sql1 = "select * from films as f join mpa as m on f.mpa = m.mpa_id and f.id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql1, filmId);
        if(filmRows.next()) {
            Film film = jdbcTemplate.queryForObject(sql1, (rs, rowNum) -> mapRowToFilm(rs, rowNum), filmId);
            log.info("???????????? ??????????: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("?????????? ?? id: {} ???? ????????????.", filmId);
            return Optional.empty();
        }
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("update films set name = ?, description = ?, release_date = ?, " +
                        "duration = ?, rate = ?, mpa = ? where id = ?", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), 0, film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sqlQuery = "select * from films as f join mpa as m on f.mpa = m.mpa_id " +
                "order by rate desc limit ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
        return films;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int mpaId = resultSet.getInt("mpa_id");
        String mpaName = resultSet.getString("mpa_name");
        Mpa mpa = new Mpa(mpaId, mpaName);
        Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));

        Film film = Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpa)
                .rate(resultSet.getInt("rate"))
                .genres(genres)
                .build();
        return film;
    }
}