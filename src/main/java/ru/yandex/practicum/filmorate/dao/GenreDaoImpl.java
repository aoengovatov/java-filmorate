package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

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
        if(!film.getGenres().isEmpty()){
            for (Genre genre : film.getGenres()){
                String sql = "insert into film_genres (film_id, genre_id)" +
                        " values (?,?)";
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            }
        }
    }
}
