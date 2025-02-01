package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);

    List<Film> getFilms();

    Film updateFilm(Film film);

    Film getFilm(Long id);

    Set<Genre> getFilmGenres(Long filmId);

    Mpa getRatingById(Integer rId);

    List<Film> getPopularFilms(int count);

    List<Film> getFilmsByDirector(Long directorId, String sortBy);

    Director getDirectorOfTheFilm(Long id);

    void removeFilm(long id);
}
