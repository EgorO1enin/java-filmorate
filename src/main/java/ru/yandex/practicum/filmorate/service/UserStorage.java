package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> getUsers();
    public User addUser(User user);
    public User updateUser(User updatedUser);
}
