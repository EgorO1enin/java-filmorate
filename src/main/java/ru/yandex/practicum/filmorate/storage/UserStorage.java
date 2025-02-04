package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationEvent;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> getUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User updatedUser);

    void removeUser(long id);

    List<Feed> getFeed(long id);

    void addFeed(long userId, EventType eventType, OperationEvent operationEvent, long entityId);
}
