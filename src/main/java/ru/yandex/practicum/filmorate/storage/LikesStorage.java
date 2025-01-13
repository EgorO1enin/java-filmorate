package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikesStorage {

    public List<Long> getLikes(Long filmId);

    public void likeFilmByuser(Long filmId, Long userId);

    public void deleteLike(Long filmId, Long userId);
}
