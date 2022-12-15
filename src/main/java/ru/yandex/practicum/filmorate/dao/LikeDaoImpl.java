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
    public void deleteByFilmId(long filmId) {
        String sql = "delete from film_likes where film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void addByFriendList(long filmId, Set<Long> friendList) {
        deleteByFilmId(filmId);
        String sql = "insert into film_likes(user_id, film_id) values (?,?)";
        for(long userId : friendList){
            jdbcTemplate.update(sql, userId, filmId);
        }
        String sql2 = "update films f set rate = (select count(l.user_id) from " +
                "film_likes l where l.film_id = f.id) where f.id = ?";
        jdbcTemplate.update(sql2, filmId);
    }

    @Override
    public Set<Long> getByFilmId(long filmId) {
        String sql = "select user_id from film_likes where film_id = ?";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sql, filmId);
        Set<Long> likes = new HashSet<>();
        if(likeRows.next()) {
            likes.add(likeRows.getLong("user_id"));
        }
        return likes;
    }
}
