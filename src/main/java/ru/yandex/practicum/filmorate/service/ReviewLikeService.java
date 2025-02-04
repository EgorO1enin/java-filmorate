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
        reviewLikeStorage.addLike(reviewId, userId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.ADD, reviewId);
    }

    public void addDislike(Long reviewId, Long userId) {
        reviewLikeStorage.addDislike(reviewId, userId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.ADD, reviewId);
    }

    public void removeLike(Long reviewId, Long userId) {
        reviewLikeStorage.removeLike(reviewId, userId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.REMOVE, reviewId);
    }

    public void removeDislike(Long reviewId, Long userId) {
        reviewLikeStorage.removeDislike(reviewId, userId);
        userDbStorage.addFeed(userId, EventType.LIKE, OperationEvent.REMOVE, reviewId);
    }
}
