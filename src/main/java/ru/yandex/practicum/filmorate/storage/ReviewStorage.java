package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    List<Review> getReviews(Long filmId, int count);

    Review getReviewById(Long reviewId);

    Review addReview(Review review);

    Review updateReview(Review review);

    void removeReview(Long reviewId);
}
