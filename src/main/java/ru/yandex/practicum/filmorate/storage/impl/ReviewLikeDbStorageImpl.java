package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;

import java.util.List;

@Component(value = "reviewLikeDbStorage")
@RequiredArgsConstructor
public class ReviewLikeDbStorageImpl implements ReviewLikeStorage {
    private final UserService userService;
    private final ReviewService reviewService;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long reviewId, Long userId) {
        User user = userService.getUserById(userId);
        Review review = reviewService.getReviewById(reviewId);
        if (user == null || review == null) {
            throw new NotFoundException("User or Review not found");
        }
        String sql = "INSERT INTO review_likes (review_id, user_id, is_like) " +
                "VALUES (?, ?, TRUE)";
        jdbcTemplate.update(sql, reviewId, userId);
        if (getUseful(reviewId) + 1 == 0) {
            review.setUseful(1L);
            reviewService.updateReview(review);
        } else {
            increaseUseful(reviewId);
        }
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        User user = userService.getUserById(userId);
        Review review = reviewService.getReviewById(reviewId);
        if (user == null || review == null) {
            throw new NotFoundException("User or Review not found");
        }
        String sql = "INSERT INTO review_likes (review_id, user_id, is_like) " +
                "VALUES (?, ?, FALSE)";
        jdbcTemplate.update(sql, reviewId, userId);
        if (getUseful(reviewId) - 1 == 0) {
            review.setUseful(-1L);
            reviewService.updateReview(review);
        } else {
            decreaseUseful(reviewId);
        }
    }

    @Override
    public void removeLike(Long reviewId, Long userId) {
        User user = userService.getUserById(userId);
        Review review = reviewService.getReviewById(reviewId);
        if (user == null || review == null) {
            throw new NotFoundException("User or Review not found");
        }
        String sql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, reviewId, userId);
        decreaseUseful(reviewId);
    }

    @Override
    public void removeDislike(Long reviewId, Long userId) {
        User user = userService.getUserById(userId);
        Review review = reviewService.getReviewById(reviewId);
        if (user == null || review == null) {
            throw new NotFoundException("User or Review not found");
        }
        String sql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, reviewId, userId);
        increaseUseful(reviewId);
    }

    public Long getUseful(Long reviewId) {
        if (reviewId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (reviewService.getReviewById(reviewId) != null) {
            String sql = "SELECT useful FROM reviews WHERE id = ?";
            List<Long> useful = jdbcTemplate.queryForList(sql, Long.class, reviewId);
            return useful.get(0);
        } else {
            throw new NotFoundException("Отзыв с ID=" + reviewId + " не найден!");
        }
    }

    public void increaseUseful(Long reviewId) {
        String sql = "UPDATE reviews SET useful = useful + 1 WHERE id = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    public void decreaseUseful(Long reviewId) {
        String sql = "UPDATE reviews SET useful = useful - 1 WHERE id = ?";
        jdbcTemplate.update(sql, reviewId);
    }
}
