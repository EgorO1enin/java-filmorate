package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmDbStorageImpl filmDbStorage;

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore((LocalDate.of(1895, 12, 27)))) {
            throw new ValidationException("Invalid date");
        }
        filmDbStorage.addFilm(film);
        return film;
    }

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
    }

    public Film getFilm(long filmId) {
        return filmDbStorage.getFilm(filmId);
    }

    public Film updateFilm(Film film) {
        filmDbStorage.updateFilm(film);
        return film;
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmDbStorage.getPopularFilms(count);
    }

    public void removeFilm(long id) {
        filmDbStorage.removeFilm(id);
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return filmDbStorage.getCommonFilms(userId, friendId);
    }
}
