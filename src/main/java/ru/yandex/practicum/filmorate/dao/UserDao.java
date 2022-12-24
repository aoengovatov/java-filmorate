package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getById(long id);

    User create(User user);

    void update(User user);

    void delete(User user);

    List<User> getAll();

    long getSize();
}