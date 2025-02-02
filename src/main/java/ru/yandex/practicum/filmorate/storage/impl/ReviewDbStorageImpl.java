package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Component(value = "reviewDbStorage")
@RequiredArgsConstructor
public class ReviewDbStorageImpl implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final FilmService filmService;
    private final ReviewRowMapper reviewRowMapper;

    @Override
    public List<Review> getReviews(Long filmId, int count) {
        String sql = "SELECT * FROM reviews " +
                "WHERE film_id = ? " +
                "ORDER BY useful " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, reviewRowMapper, filmId, count);
    }

    @Override
    public Review getReviewById(Long reviewId) {
        if (reviewId == null) {
            throw new ValidationException("Review Id не может быть null");
        }
        String sql = "SELECT * FROM reviews WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, reviewRowMapper, reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Отзыв с ID=" + reviewId + " не найден!");
        }
    }

    @Override
    public Review addReview(Review review) {
        User user = userService.getUserById(review.getUserId());
        Film film = filmService.getFilm(review.getFilmId());
        if (user == null || film == null) {
            throw new NotFoundException("User or Film not found");
        }
        review.setUseful(0L);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");
        try {
            review.setReviewId(simpleJdbcInsert.executeAndReturnKey(review.toMap()).longValue());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при добавлении отзыва");
        }
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        if (review == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (getReviewById(review.getReviewId()) != null) {
            if (review.getUseful() == null) {
                review.setUseful(0L);
            }
            String sql = "UPDATE reviews SET " +
                    "content = ?, " +
                    "is_positive = ?, " +
                    "film_id = ?, " +
                    "user_id = ?, " +
                    "useful = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sql,
                    review.getContent(),
                    review.getIsPositive(),
                    review.getFilmId(),
                    review.getUserId(),
                    review.getUseful(),
                    review.getReviewId());
            return review;
        } else {
            throw new NotFoundException("Отзыв с ID=" + review.getReviewId() + " не найден!");
        }
    }

    @Override
    public void removeReview(Long reviewId) {
        if (reviewId == null) {
            throw new ValidationException("Review Id не может быть null");
        }
        if (getReviewById(reviewId) != null) {
            String sql = "DELETE FROM reviews WHERE id = ?";
            jdbcTemplate.update(sql, reviewId);
        } else {
            throw new NotFoundException("Отзыв с ID=" + reviewId + " не найден!");
        }
    }
}
