package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

@Service
public class GenreService {
    private final GenreDbStorageImpl genreDbStorage;

    public GenreService(final GenreDbStorageImpl genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenreById(Long genreId) {
        return genreDbStorage.getGenreById(genreId);
    }

    public void addGenere(Film film) {
        genreDbStorage.addGenere(film);
    }

    public void deleteGenere(Film film) {
        genreDbStorage.deleteGenre(film);
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        return genreDbStorage.getFilmGenres(filmId);

    }
}