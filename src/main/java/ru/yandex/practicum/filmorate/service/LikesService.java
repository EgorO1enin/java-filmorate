package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDbStorage;

import java.util.List;

@Service
public class LikesService {
    private final LikesDbStorage likesDbStorage;

    public LikesService(final LikesDbStorage likesDbStorage) {
        this.likesDbStorage = likesDbStorage;
    }

    public void addLike(Long userId, Long filmId){
        likesDbStorage.likeFilmByuser(userId, filmId);
    }

   public void removeLike(Long userId, Long filmId) {
       likesDbStorage.deleteLike(userId, filmId);
   }

    public List<Long> getLikes(Long filmId) {
        return likesDbStorage.getLikes(filmId);
    }
}
