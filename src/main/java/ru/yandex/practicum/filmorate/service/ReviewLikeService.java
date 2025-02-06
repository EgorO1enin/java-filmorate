package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationEvent;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

@RequiredArgsConstructor
@Service
public class ReviewLikeService {
    private final ReviewLikeStorage reviewLikeStorage;
    private final UserDbStorageImpl userDbStorage;

    public void addLike(Long reviewId, Long userId) {
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.ADD, reviewId);
        reviewLikeStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.ADD, reviewId);
        reviewLikeStorage.addDislike(reviewId, userId);
    }

    public void removeLike(Long reviewId, Long userId) {
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.REMOVE, reviewId);
        reviewLikeStorage.removeLike(reviewId, userId);
    }

    public void removeDislike(Long reviewId, Long userId) {
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.REMOVE, reviewId);
        reviewLikeStorage.removeDislike(reviewId, userId);
    }
}
