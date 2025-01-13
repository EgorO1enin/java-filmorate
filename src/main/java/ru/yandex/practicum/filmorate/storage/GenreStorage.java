package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    public void addGenere(Film film);

    public void addFilmGenre(long filmId, Long genreId);

    public Genre getGenreById(Long genreId);

    public List<Genre> getAllGenres();

    public void deleteGenre(Film film);

    public Set<Genre> getFilmGenres(Long genreId);
}
