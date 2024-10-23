package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.*;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;


    public Film likeFilm(long filmId, long userId) {
        if (inMemoryUserStorage.getUserById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        if (inMemoryFilmStorage.getFilm(filmId) == null) {
            throw new NotFoundException("Film not found");
        }
        inMemoryFilmStorage.getFilm(filmId).getLikes().add(userId);
        return inMemoryFilmStorage.getFilm(filmId);
    }

    public String deleteLike(long filmId, long userId) {
        if (inMemoryFilmStorage.getFilm(filmId) == null) {
            throw new NotFoundException("Film not found");
        }
        if (inMemoryUserStorage.getUserById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        inMemoryFilmStorage.getFilm(filmId).getLikes().remove(userId);
        return "like deleted";
    }

    public Collection<Film> getPopularFilms(int size) {
        return inMemoryFilmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesLength).reversed()).limit(size).toList();

    }
}
