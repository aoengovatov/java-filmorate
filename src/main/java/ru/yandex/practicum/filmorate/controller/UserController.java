package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;
    @GetMapping
    public Collection<User> getUsers(){
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        int id = generateId();
        user.setId(id);
        if(user.getName() == null){
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if(users.containsKey(user.getId())){
            if(user.getName() == null){
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Ошибка. Нет пользователя с id: " + user.getId());
        }
        return user;
    }

    private int generateId(){
        return ++id;
    }
}