package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class LikeDaoImpl implements LikeDao{

    private JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "merge into film_likes (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public Set<Long> getAll(long filmId) {
        String sql = "select user_id from film_likes where film_id = ?";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sql, filmId);
        Set<Long> likes = new HashSet<>();
        if(likeRows.next()) {
            likes.add(likeRows.getLong("user_id"));
        }
        return likes;
    }

    private void updateRate(long filmId) {
        String sqlQuery = "update films f set rate = (select count(l.user_id) from " +
                "film_likes l where l.film_id = f.id)  where f.id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
