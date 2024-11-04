package ru.yandex.practicum.filmorate.dao.inter;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> getUsers();

    User getUserById(Long id);

    public User addUser(User user);

    public User updateUser(User updatedUser);
}
