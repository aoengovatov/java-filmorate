package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql({"/schema.sql"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDaoImplTest {

    private final UserService userService;
    private final UserDao userDao;
    private final FriendDao friendDao;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.dropTables(jdbcTemplate, "film_genres", "film_likes", "films",
                "friends", "users");
    }

    @Test
    public void createUser() {
        data();
        Optional<User> optionalUser =  userDao.getById(1);
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "mail@mail.ru"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "testLogin"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "testName"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1968,12,24)));
    }

    @Test
    public void updateUser() {
        data();
        User userUpdate = new User(1,"mailupdate@mail.ru", "testLoginUpdate", "testNameUpdate",
                LocalDate.of(1967,11,22));
        userDao.update(userUpdate);

        Optional<User> userOptional = userDao.getById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "mailupdate@mail.ru"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "testLoginUpdate"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "testNameUpdate"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1967,11,22)));
    }

    @Test
    public void deleteUser() {
        data();
        Optional<User> userOptional1 = userDao.getById(1);
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1));

        User userDelete = new User(1,"mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));
        userDao.delete(userDelete);

        Optional<User> userOptional2 = userDao.getById(1);
        assertThat(userOptional2)
                .isEmpty();
    }

    @Test
    public void getUserById() {
        data();
        Optional<User> userOptional1 = userDao.getById(1);
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1));
    }

    @Test
    public void getUsers() {
        data();
        User user2 = new User(2,"mail@mail.ru", "testLogin2", "testName2",
                LocalDate.of(1967,11,22));
        userDao.create(user2);

        Collection<User> users = userDao.getAll();
        assertEquals(users.size(), 2);
    }

    @Test
    public void addUser1InFriendsUser2() {
        data();
        User user2 = new User(2,"mail@mail.ru", "testLogin2", "testName2",
                LocalDate.of(1967,11,22));
        userDao.create(user2);
        List<User> user1Friends = friendDao.getFriends(1);
        assertNotNull(user1Friends);
        assertEquals(user1Friends.size(), 0);
        userService.addFriend(1, user2.getId());
        List<User> user1FriendsTest = friendDao.getFriends(1);
        assertNotNull(user1FriendsTest);
        assertEquals(user1FriendsTest.size(), 1);

        List<User> user2FriendsTest = friendDao.getFriends(2);
        assertNotNull(user2FriendsTest);
        assertEquals(user2FriendsTest.size(), 0);
    }

    @Test
    public void addUser2AndUser1AddFriends() {
        User user1 = new User(1,"mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));
        userDao.create(user1);
        User user2 = new User(2,"mail@mail.ru", "testLogin2", "testName2",
                LocalDate.of(1967,11,22));
        userDao.create(user2);
        List<User> user1Friends = friendDao.getFriends(1);
        List<User> user2Friends = friendDao.getFriends(2);
        assertNotNull(user1Friends);
        assertEquals(user1Friends.size(), 0);
        assertNotNull(user2Friends);
        assertEquals(user2Friends.size(), 0);
        userService.addFriend(1, user2.getId());
        userService.addFriend(2, user1.getId());
        List<User> user1FriendsTest = friendDao.getFriends(1);
        assertNotNull(user1FriendsTest);
        assertEquals(user1FriendsTest.size(), 1);
        List<User> user2FriendsTest = friendDao.getFriends(2);
        assertNotNull(user2FriendsTest);
        assertEquals(user2FriendsTest.size(), 1);
    }

    @Test
    public void deleteUser1InFriendsUser2() {
        data();
        User user2 = new User(2,"mail@mail.ru", "testLogin2", "testName2",
                LocalDate.of(1967,11,22));
        userDao.create(user2);
        List<User> user1Friends = friendDao.getFriends(1);
        assertNotNull(user1Friends);
        assertEquals(user1Friends.size(), 0);
        userService.addFriend(1, user2.getId());
        List<User> user1FriendsTest = friendDao.getFriends(1);
        assertNotNull(user1FriendsTest);
        assertEquals(user1FriendsTest.size(), 1);
        userService.deleteFriend(1, user2.getId());
        List<User> user1FriendsTest2 = friendDao.getFriends(1);
        assertNotNull(user1FriendsTest2);
        assertEquals(user1FriendsTest2.size(), 0);
    }

    @Test
    public void getUsersCount() {
        data();
        User user2 = new User(1,"mail@mail.ru", "testLogin2", "testName2",
                LocalDate.of(1967,11,22));
        userDao.create(user2);
        long users = userDao.getSize();
        assertEquals(users, 2);
    }

    private void data(){
        User user1 = new User(1,"mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));
        userDao.create(user1);
    }
}