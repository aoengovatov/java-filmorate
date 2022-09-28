package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users;
    private final Map<Long, Set<Long>> friends;

    @Override
    public void add(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        if(users.containsKey(user.getId())){
            users.put(user.getId(), user);
        }
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public Integer getSize() {
        return users.size();
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Set<Long> getUserFriends(long userId) {
        return friends.get(userId);
    }

    @Override
    public void updateFriends(long id, Set<Long> friendList) {
        friends.put(id, friendList);
    }
}