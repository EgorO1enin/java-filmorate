package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDbStorage userDbStorage;

    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userDbStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userDbStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }

    public User addFriend(Long userid, Long friendId) {
        userDbStorage.addFriend(userid, friendId);
        return getUserById(friendId);
    }

    public List<User> getFriends(Long userid) {
        return userDbStorage.getFriends(userid);
    }

    public User removeFriend(Long userid, Long friendId) {
        userDbStorage.removeFriend(userid, friendId);
        return getUserById(friendId);
    }

    public Collection<User> getCommonFriends(Long userid, Long friendId) {
        return userDbStorage.getCommonFriends(userid, friendId);
    }
}
