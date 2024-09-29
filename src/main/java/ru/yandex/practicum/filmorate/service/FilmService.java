package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.*;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

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
        if (inMemoryUserStorage.getUserById(userId) == null) {
            throw new ValidationException("User not found");
        }
        if (inMemoryFilmStorage.getFilm(filmId) == null) {
            throw new ValidationException("Film not found");
        }
        inMemoryFilmStorage.getFilm(filmId).getLikes().remove(userId);
        return "like deleted";
    }

    public Collection<Film> getPopularFilms(int size) {
        return inMemoryFilmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getLikesLength).reversed()).limit(size).toList();

    }
}
