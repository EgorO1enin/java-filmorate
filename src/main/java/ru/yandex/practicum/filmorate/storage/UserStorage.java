package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

interface UserStorage {
    public Collection<User> getUsers();

    User getUserById(Long id);

    public User addUser(User user);

    public User updateUser(User updatedUser);
}
