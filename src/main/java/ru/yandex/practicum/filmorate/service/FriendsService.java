package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FriendsDbStorageImpl;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendsDbStorageImpl friendsDbStorage;
    private final UserService userService;

    public User addFriend(Long userid, Long friendId) {
        friendsDbStorage.addFriend(userid, friendId);
        return userService.getUserById(friendId);
    }

    public List<User> getFriends(Long userid) {
        return friendsDbStorage.getFriends(userid);
    }

    public User removeFriend(Long userid, Long friendId) {
        friendsDbStorage.removeFriend(userid, friendId);
        return userService.getUserById(friendId);
    }

    public Collection<User> getCommonFriends(Long userid, Long friendId) {
        return friendsDbStorage.getCommonFriends(userid, friendId);
    }
}
