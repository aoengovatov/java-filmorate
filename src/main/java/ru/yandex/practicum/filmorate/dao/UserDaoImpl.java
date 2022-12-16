package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class UserDaoImpl implements UserDao{

    private JdbcTemplate jdbcTemplate;
    public UserDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if(userRows.next()) {
            User user = new User(
                    userRows.getLong("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users (email,login,name,birthday) " +
                "values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        user.setId(id);
        return user;
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("update users set email = ?, login = ?, name = ?, " +
                        "birthday = ? where id = ?", user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update("delete from users where id = ?", user.getId());
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

//    @Override
//    public Set<Long> getUserFriends(long userId) {
//        String sqlQuery = "select friend_id from friends where user_id = " + userId;
//        List<Long> friends = jdbcTemplate.queryForList(sqlQuery, Long.class);
//        return new HashSet<>(friends);
//    }
//
//    @Override
//    public void updateFriends(long id, long friendId, String status) {
//        jdbcTemplate.update("insert into friends (user_id, friend_id, status) values (?,?,?)",
//                id, friendId, status);
//    }
//
//    @Override
//    public void deleteFriend(long id, long userFriend) {
//        jdbcTemplate.update("delete friends where user_id = ? and friend_id = ?", id, userFriend);
//    }

    @Override
    public long getSize() {
        String sqlQuery = "select count(*) from users";
        return jdbcTemplate.queryForObject(
                sqlQuery, Long.class);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException  {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}