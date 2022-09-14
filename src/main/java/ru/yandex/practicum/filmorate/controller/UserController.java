package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.split;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;
    @GetMapping
    public Collection<User> getUsers(){
        log.info("Количество пользователей: " + users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        int id = generateId();
        user.setId(id);
        if(user.getName() == null){
            user.setName(user.getLogin());
        }
        String[] login = user.getLogin().split(" ");
        if(login != null && login.length > 1){
            throw new ValidationException("Ошибка. Логин не должен содержать пробелы");
        }
        users.put(id, user);
        log.info("Создан пользователь: " + user);
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
            log.warn("Нет пользователя с id: " + user.getId());
            throw new ValidationException("Ошибка. Нет пользователя с id: " + user.getId());
        }
        log.info("Обновлен пользователь: " + user);
        return user;
    }

    private int generateId(){
        return ++id;
    }
}