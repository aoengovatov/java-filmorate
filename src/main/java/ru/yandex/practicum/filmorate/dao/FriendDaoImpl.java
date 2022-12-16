package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FriendDaoImpl implements FriendDao{

    private JdbcTemplate jdbcTemplate;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Long> getFriends(long userId) {
        String sqlQuery = "select friend_id from friends where user_id = " + userId;
        List<Long> friends = jdbcTemplate.queryForList(sqlQuery, Long.class);
        return new HashSet<>(friends);
    }

    public void updateFriends(long id, long friendId, String status) {
        jdbcTemplate.update("insert into friends (user_id, friend_id, status) values (?,?,?)",
                id, friendId, status);
    }

    public void deleteFriend(long id, long userFriend) {
        jdbcTemplate.update("delete friends where user_id = ? and friend_id = ?", id, userFriend);
    }
}
