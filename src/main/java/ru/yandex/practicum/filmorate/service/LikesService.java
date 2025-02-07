package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationEvent;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.LikesDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.util.List;

@Service
@Slf4j
public class LikesService {
    private final LikesDbStorageImpl likesDbStorage;
    private final UserDbStorageImpl userDbStorage;

    public LikesService(final LikesDbStorageImpl likesDbStorage, UserDbStorageImpl userDbStorage) {
        this.likesDbStorage = likesDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public void addLike(Long filmId, Long userId) {
        likesDbStorage.likeFilmByuser(filmId, userId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.ADD, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        likesDbStorage.deleteLike(filmId, userId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.REMOVE, filmId);
    }

    public List<Long> getLikes(Long filmId) {
        return likesDbStorage.getLikes(filmId);
    }
}
