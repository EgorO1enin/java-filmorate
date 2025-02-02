package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationEvent;
import ru.yandex.practicum.filmorate.storage.impl.LikesDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.util.List;

@Service
public class LikesService {
    private final LikesDbStorageImpl likesDbStorage;
    private final UserDbStorageImpl userDbStorage;

    public LikesService(final LikesDbStorageImpl likesDbStorage, UserDbStorageImpl userDbStorage) {
        this.likesDbStorage = likesDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public void addLike(Long userId, Long filmId) {
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.ADD, filmId);
        likesDbStorage.likeFilmByuser(userId, filmId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.ADD, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.REMOVE, filmId);
        likesDbStorage.deleteLike(userId, filmId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.REMOVE, filmId);
    }

    public List<Long> getLikes(Long filmId) {
        return likesDbStorage.getLikes(filmId);
    }
}
