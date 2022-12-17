package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {

    private JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteInFilm(long filmId){
        String sql = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void updateInFilm(Film film) {
        if(film.getGenres() != null){
            String sql = "insert into film_genres (film_id, genre_id) values (?,?)";
            jdbcTemplate.batchUpdate(sql, film.getGenres(), film.getGenres().size(),
                    (ps, genre) -> {
                        ps.setLong(1, film.getId());
                        ps.setInt(2, genre.getId());
                    });
        }
    }

    @Override
    public List<Film> loadGenres(List<Film> films) {
        if(films != null && !films.isEmpty()) {
            for(Film film : films){
                loadGenresByFilm(film);
            }
//            final Map<Long, Film> filmMap = films.stream()
//                    .collect(Collectors.toMap(film -> film.getId(), film -> film, (a, b) -> b));
//            final List<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
//            String sql = "select film_id fg, genre_id fg, name g from film_genres fg " +
//            "join genre g on fg.genre_id = g.id  where fg.film_id in (:ids)";
//            SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
//            SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, parameters);
//            if (genreRows.next()) {
//                int filmId = genreRows.getInt("film_id");
//                Set<Genre> genres = filmMap.get(filmId).getGenres();
//                genres.add(new Genre(genreRows.getInt("genre_id"), "null"));
//                filmMap.get(filmId).setGenres(genres);
//            }
        }
        return films;
    }

    @Override
    public Film loadGenresByFilm(Film film) {
        if(film != null){
            String sql = "select * from genre where id in (select genre_id from film_genres " +
                    "where film_id = ?)";
            List<Genre> genresByFilm = jdbcTemplate.query(sql, this::mapRowToGenre, film.getId());
            Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            genres.addAll(genresByFilm);
            film.setGenres(genres);
        }
        return film;
    }

    @Override
    public Collection<Genre> getAll() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);

    }

    @Override
    public Optional<Genre> getById(int genreId) {
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

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}