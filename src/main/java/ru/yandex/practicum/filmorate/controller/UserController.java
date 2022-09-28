package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public Collection<User> getUsers(){
        return userService.getUsers();
    }
    @GetMapping( "/{id}")
    public User getUsers(@PathVariable Integer id){
        if(id <= 0){
            throw new IncorrectParameterException("id");
        }
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Integer id){
        if(id <= 0){
            throw new IncorrectParameterException("id");
        }
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId){
        if(id <= 0){
            throw new IncorrectParameterException("id");
        }
        if(otherId <= 0){
            throw new IncorrectParameterException("otherId");
        }
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User add(@Valid @RequestBody User user){
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if(user.getId() <= 0){
            throw new IncorrectParameterException("id");
        }
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId){
        if(id <= 0){
            throw new IncorrectParameterException("id");
        }
        if(friendId <= 0){
            throw new IncorrectParameterException("friendId");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId){
        if(id <= 0){
            throw new IncorrectParameterException("id");
        }
        if(friendId <= 0){
            throw new IncorrectParameterException("friendId");
        }
        userService.deleteFriend(id, friendId);
    }
}