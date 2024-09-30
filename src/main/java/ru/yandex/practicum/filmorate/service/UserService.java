package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryFilmStorage) {
        this.inMemoryUserStorage = inMemoryFilmStorage;
    }

    public Collection<User> addFriend(long userid, long friendId) {
        User user = inMemoryUserStorage.getUserById(userid);
        User friend = inMemoryUserStorage.getUserById(friendId);

        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (friend == null) {
            throw new NotFoundException("Friend not found");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userid);

        return inMemoryUserStorage.getUsers();
    }

    public Collection<User> getFriends(long userid) {
        Collection<User> friends = new ArrayList<>();
        if (inMemoryUserStorage.getUserById(userid) == null) {
            throw new NotFoundException("User or friend not found");
        }
        for (Long id : inMemoryUserStorage.getUserById(userid).getFriends()) {
            friends.add(inMemoryUserStorage.getUserById(id));
        }
        return friends;
    }

    public String deleteFriend(long userid, long friendId) {
        User user = inMemoryUserStorage.getUserById(userid);
        User friend = inMemoryUserStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new ValidationException("User or friend not found");
        }
        if (!user.getFriends().contains(friendId)) {
            throw new ValidationException("User is not friend");
        }
        user.getFriends().remove(friendId);
        return "removed from friends";
    }

    public Collection<User> getCommonFriends(long userid, long friendId) {
        User user = inMemoryUserStorage.getUserById(userid);
        User friend = inMemoryUserStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new ValidationException("User or friend not found");
        }
        Set<Long> commonFriends = user.getFriends();
        commonFriends.retainAll(friend.getFriends());
        Collection<User> users = new ArrayList<>();
        for (Long id : commonFriends) {
            users.add(inMemoryUserStorage.getUserById(id));
        }
        return users;
    }
}
