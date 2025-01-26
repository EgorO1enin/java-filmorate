package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikesStorage {

    List<Long> getLikes(Long filmId);

    void likeFilmByuser(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
