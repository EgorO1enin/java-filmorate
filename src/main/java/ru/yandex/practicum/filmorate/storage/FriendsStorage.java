package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FriendsStorage {
    Collection<User> getCommonFriends(Long firstUserId, Long lastUserId);

    User removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    void addFriend(Long userId, Long friendId);
}
