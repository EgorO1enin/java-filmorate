package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FriendsStorage {
    public Collection<User> getCommonFriends(Long firstUserId, Long lastUserId);

    public User removeFriend(Long userId, Long friendId);

    public List<User> getFriends(Long userId);

    public void addFriend(Long userId, Long friendId);
}
