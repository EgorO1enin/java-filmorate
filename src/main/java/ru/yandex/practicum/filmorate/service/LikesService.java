package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.LikesDbStorageImpl;

import java.util.List;

@Service
public class LikesService {
    private final LikesDbStorageImpl likesDbStorage;

    public LikesService(final LikesDbStorageImpl likesDbStorage) {
        this.likesDbStorage = likesDbStorage;
    }

    public void addLike(Long userId, Long filmId) {
        likesDbStorage.likeFilmByuser(userId, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        likesDbStorage.deleteLike(userId, filmId);
    }

    public List<Long> getLikes(Long filmId) {
        return likesDbStorage.getLikes(filmId);
    }
}
