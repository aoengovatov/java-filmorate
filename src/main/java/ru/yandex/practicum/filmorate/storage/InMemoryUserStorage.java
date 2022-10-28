package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;
    private final Map<Long, Set<Long>> friends;

    private long id = 0;

    @Override
    public User add(User user) {
        long id = generateId();
        user.setId(id);
        users.put(id, user);
        Set<Long> userFriends = new HashSet<>();
        friends.put(id, userFriends);
        return user;
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
    public long getSize() {
        return users.size();
    }

    @Override
    public List<User> getUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public Set<Long> getUserFriends(long userId) {
        return friends.get(userId);
    }

    @Override
    public Optional<User> getUserById(long userId){
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void updateFriends(long id, Set<Long> friendList) {
        friends.computeIfAbsent(id, u -> friendList);
    }

    @Override
    public void deleteFriend(long id, long userFriend) {

    }

    private long generateId(){
        return ++id;
    }
}