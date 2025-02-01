package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDbStorageImpl userDbStorage;

    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userDbStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userDbStorage.addUser(user);
    }

    public void updateUser(User user) {
        userDbStorage.updateUser(user);
    }

    public void removeUser(long id) {
        userDbStorage.removeUser(id);
    }
}
