package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
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

    public List<User> getFriends(long userId) {
        String sql = "select * from users where id in (select friend_id from friends where user_id = ?)";
        return jdbcTemplate.query(sql, this::mapRowToUser, userId);
    }

    public void updateFriends(long id, long friendId, String status) {
        jdbcTemplate.update("insert into friends (user_id, friend_id, status) values (?,?,?)",
                id, friendId, status);
    }

    public void deleteFriend(long id, long userFriend) {
        jdbcTemplate.update("delete friends where user_id = ? and friend_id = ?", id, userFriend);
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        String sql = "select * from users where id in (select friend_id from friends where user_id = ? OR " +
                "user_id = ? group by friend_id having count(*) = 2);";
        return jdbcTemplate.query(sql, this::mapRowToUser, id, otherId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}