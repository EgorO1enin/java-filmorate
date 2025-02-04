package ru.yandex.practicum.filmorate.storage;

public interface ReviewLikeStorage {
    void addLike(Long reviewId, Long userId);

    void addDislike(Long reviewId, Long userId);

    void removeLike(Long reviewId, Long userId);

    void removeDislike(Long reviewId, Long userId);
}
