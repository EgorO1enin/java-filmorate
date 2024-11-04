package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.dao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FriendsService friendsService;


    @Autowired
    public UserController(UserService userService, FriendsService friendsService) {
        this.userService = userService;
        this.friendsService = friendsService;
    }

    @GetMapping()
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping()
    public User addUser(@RequestBody @Validated User user) {
        return userService.addUser(user);
    }

    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated User updatedUser) {
        return userService.updateUser(updatedUser);
    }

    @PutMapping("/{id}/friends/{friend_id}")
    public User addFriend(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {
        return friendsService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return friendsService.getFriends(id);
    }

    @DeleteMapping("/{id}/friends/{friend_id}")
    public User removeFriend(@PathVariable("id") long id, @PathVariable("friend_id") long friendId) {
        return friendsService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return friendsService.getCommonFriends(id, otherId);
    }
}
