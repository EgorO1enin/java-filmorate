package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;

@RequiredArgsConstructor
@Service

@Slf4j
public class ReviewLikeService {
    private final ReviewLikeStorage reviewLikeStorage;

    public void addLike(Long reviewId, Long userId) {
        reviewLikeStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        reviewLikeStorage.addDislike(reviewId, userId);
    }

    public void removeLike(Long reviewId, Long userId) {
        reviewLikeStorage.removeLike(reviewId, userId);
    }

    public void removeDislike(Long reviewId, Long userId) {
        reviewLikeStorage.removeDislike(reviewId, userId);
    }
}
