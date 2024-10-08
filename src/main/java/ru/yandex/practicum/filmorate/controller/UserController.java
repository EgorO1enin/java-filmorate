package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @GetMapping()
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping()
    public User addUser(@RequestBody @Validated User user) {
        user.setId(id);
        id++;
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Добавлен пользователь{}", user);
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody @Validated User updatedUser) {
        User oldUser = users.get(updatedUser.getId());
        updatedUser.setId(oldUser.getId());
        users.put(updatedUser.getId(), updatedUser);
        log.info("Обновлен пользователь{}", updatedUser);
        return updatedUser;
    }
}
