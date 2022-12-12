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
            stmt.setInt(6, film.getRate());
            return stmt;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();

        if(!film.getGenres().isEmpty()){
            for (Genre genre : film.getGenres()){
                String sql = "insert into film_genres (film_id, genre_id) " +
                        "values (?,?)";
                jdbcTemplate.update(sql, id, genre.getId());
            }
        }
        return getFilmById(id).get();
    }

    @Override
    public long getSize() {
        String sqlQuery = "select count(*) from films";
        return jdbcTemplate.queryForObject(
                sqlQuery, Long.class);
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        String sql1 = "select * from films where id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql1, filmId);
        if(filmRows.next()) {
            Film film = jdbcTemplate.queryForObject(sql1, (rs, rowNum) -> mapRowToFilm(rs, rowNum), filmId);
            log.info("РќР°Р№РґРµРЅ С„РёР»СЊРј: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("РџРѕР»СЊР·РѕРІР°С‚РµР»СЊ СЃ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂРѕРј {} РЅРµ РЅР°Р№РґРµРЅ.", filmId);
            return Optional.empty();
        }
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("update films set name = ?, description = ?, release_date = ?, " +
                        "duration = ?, rate = ?, mpa = ? where id = ?", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update("delete from film_genres where film_id = ?", film.getId());
        if(!film.getGenres().isEmpty()){
            for (Genre genre : film.getGenres()){
                String sql = "insert into film_genres (film_id, genre_id)" +
                        " values (?,?)";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId()).get();
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sqlQuery = "select * from films order by rate desc limit ?";
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

            log.info("РќР°Р№РґРµРЅ Р¶Р°РЅСЂ: {} {}", genre.getId(), genre.getName());

            return Optional.of(genre);
        } else {
            log.info("Р–Р°РЅСЂ СЃ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂРѕРј {} РЅРµ РЅР°Р№РґРµРЅ.", genreId);
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
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from mpa where id = ?", mpaId);
        if(genreRows.next()) {
            Mpa mpa = new Mpa(
                    genreRows.getInt("id"),
                    genreRows.getString("name"));

            log.info("РќР°Р№РґРµРЅ Р¶Р°РЅСЂ: {} {}", mpa.getId(), mpa.getName());

            return Optional.of(mpa);
        } else {
            log.info("Р–Р°РЅСЂ СЃ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂРѕРј {} РЅРµ РЅР°Р№РґРµРЅ.", mpaId);
            return Optional.empty();
        }
    }

    @Override
    public Set<Long> getLikesById(long id) {
        String sql3 = "select user_id from film_likes where film_id = ?";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sql3, id);
        Set<Long> likes = new HashSet<>();
        if(likeRows.next()) {
            likes.add(likeRows.getLong("user_id"));
        }
        return likes;
    }

    @Override
    public void updateLikes(long id, Set<Long> friendList, int rate) {
        if(rate == -1){
            String sql4 = "delete from film_likes where film_id = ?";
            jdbcTemplate.update(sql4, id);
        }
        for(long userId : friendList){
            String sql4 = "insert into film_likes(user_id, film_id) values (?,?)";
            jdbcTemplate.update(sql4, id, userId);
        }
        String sqlQ = "select rate from films where id = ?";
        Integer totalFilmRate = jdbcTemplate.queryForObject(sqlQ, Integer.class, id);
        totalFilmRate += rate;
        String sql = "update films set rate = ? where id = ?";
        jdbcTemplate.update(sql, totalFilmRate, id);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Integer mpaId = resultSet.getInt("mpa");
        String sqlQ = "select name from mpa where id = ?";
        String mpaName = jdbcTemplate.queryForObject(sqlQ, String.class, mpaId);
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
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
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