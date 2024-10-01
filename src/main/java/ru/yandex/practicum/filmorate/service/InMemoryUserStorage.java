package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(id);
        id++;
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Добавлен пользователь{}", user);
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        User oldUser = users.get(updatedUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("User with id " + updatedUser.getId() + " not found");
        }
        updatedUser.setId(oldUser.getId());
        users.put(updatedUser.getId(), updatedUser);
        log.info("Обновлен пользователь{}", updatedUser);
        return updatedUser;
    }
}
