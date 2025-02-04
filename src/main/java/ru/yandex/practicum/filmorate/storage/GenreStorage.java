package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

public interface GenreStorage {
    void addGenere(Film film);

    void addFilmGenre(long filmId, Long genreId);

    Genre getGenreById(Long genreId);

    LinkedHashSet<Genre> getAllGenres();

    void deleteGenre(Film film);

    LinkedHashSet<Genre> getFilmGenres(Long genreId);
}
