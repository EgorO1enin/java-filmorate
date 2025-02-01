package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User updatedUser);

    void removeUser(long id);

    void updateUser(User updatedUser);

    User updateUser(User updatedUser);
}
