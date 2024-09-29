package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

import ru.yandex.practicum.filmorate.service.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping()
    public Collection<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    @PostMapping()
    public User addUser(@RequestBody @Validated User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody @Validated User updatedUser) {
        return inMemoryUserStorage.updateUser(updatedUser);
    }

    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return inMemoryUserStorage.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friend_id}")
    public Collection<User> addFriend(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {
        return userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") long id) {
        return userService.getFriends(id);
    }

    @DeleteMapping("/{id}/friends/{friend_id}")
    public String deleteFriend(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
