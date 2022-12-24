package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public Collection<User> getAll(){
        return userService.getAll();
    }
    @GetMapping( "/{id}")
    public User getById(@PathVariable long id){
        if(id <= 0){
            log.info("Запрос пользователя с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        return userService.getById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id){
        if(id <= 0){
            log.info("Запрос списка друзей пользователя с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId){
        if(id <= 0){
            log.info("Запрос списка общих друзей пользователя с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        if(otherId <= 0){
            log.info("Запрос списка общих друзей пользователя с неверным otherId: " + otherId);
            throw new IncorrectParameterException("otherId");
        }
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User add(@Valid @RequestBody User user){
        return userService.create(validNameAndLogin(user));
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if(user.getId() <= 0){
            log.info("Обновление пользователя с неверным id: " + user.getId());
            throw new IncorrectParameterException("id");
        }
        return userService.update(validNameAndLogin(user));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId){
        if(id <= 0){
            log.info("Добавление в друзья пользователя с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        if(friendId <= 0){
            log.info("Добавление в друзья пользователя с неверным friendId: " + friendId);
            throw new IncorrectParameterException("friendId");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId){
        if(id <= 0){
            log.info("Удаление из друзей пользователя с неверным id: " + id);
            throw new IncorrectParameterException("id");
        }
        if(friendId <= 0){
            log.info("Удаление из друзей пользователя с неверным friendId: " + friendId);
            throw new IncorrectParameterException("friendId");
        }
        userService.deleteFriend(id, friendId);
    }

    private static User validNameAndLogin(User user) {
        if(user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        if(user.getLogin().contains(" ")){
            throw new ValidationException("Ошибка. Логин пользователя не должен содержать пробелы");
        }
        return user;
    }
}