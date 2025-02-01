package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FriendsService friendsService;
    private final FilmService filmService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User addUser(@RequestBody @Validated User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable long id) {
        userService.removeUser(id);
    }

    @GetMapping("{id}/recommendations")
    public Collection<Film> getUserRecommendations(@PathVariable Long id) {
        return filmService.getUserRecommendations(id);
    }
}
