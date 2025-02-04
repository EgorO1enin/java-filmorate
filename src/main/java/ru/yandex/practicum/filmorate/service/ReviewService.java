package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationEvent;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserDbStorageImpl userDbStorage;

    public List<Review> getReviews(Long filmId, int count) {
        return reviewStorage.getReviews(filmId, count);
    }

    public Review getReviewById(Long reviewId) {
        return reviewStorage.getReviewById(reviewId);
    }

    public Review addReview(Review review) {
        userDbStorage.addFeed(review.getUserId(), EventType.REVIEW, OperationEvent.ADD, review.getReviewId());
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        userDbStorage.addFeed(review.getUserId(), EventType.REVIEW, OperationEvent.UPDATE, review.getReviewId());
        return reviewStorage.updateReview(review);
    }

    public void removeReview(Long reviewId) {
        Review review = reviewStorage.getReviewById(reviewId);
        userDbStorage.addFeed(review.getUserId(), EventType.REVIEW, OperationEvent.REMOVE, review.getReviewId());
        reviewStorage.removeReview(reviewId);
    }
}
