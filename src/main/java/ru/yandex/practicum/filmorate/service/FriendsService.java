package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationEvent;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FriendsDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendsDbStorageImpl friendsDbStorage;
    private final UserService userService;
    private final UserDbStorageImpl userDbStorage;

    public User addFriend(Long userid, Long friendId) {
        userDbStorage.addFeed(userid, EventType.FRIEND, OperationEvent.ADD, friendId);
        friendsDbStorage.addFriend(userid, friendId);
        return userService.getUserById(friendId);
    }

    public List<User> getFriends(Long userid) {
        return friendsDbStorage.getFriends(userid);
    }

    public User removeFriend(Long userid, Long friendId) {
        userDbStorage.addFeed(userid, EventType.FRIEND, OperationEvent.REMOVE, friendId);
        friendsDbStorage.removeFriend(userid, friendId);
        return userService.getUserById(friendId);
    }

    public Collection<User> getCommonFriends(Long userid, Long friendId) {
        return friendsDbStorage.getCommonFriends(userid, friendId);
    }
}
