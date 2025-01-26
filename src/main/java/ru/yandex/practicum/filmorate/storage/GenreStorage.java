package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    void addGenere(Film film);

    void addFilmGenre(long filmId, Long genreId);

    Genre getGenreById(Long genreId);

    List<Genre> getAllGenres();

    void deleteGenre(Film film);

    Set<Genre> getFilmGenres(Long genreId);
}
