package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    List<Film> getFilms();

    Film updateFilm(Film film);

    List<Director> getDirectorsByFilmId(Long filmId);

    void updateFilmDirectors(Long filmId, List<Director> directors);

    Film getFilm(Long id);

    LinkedHashSet<Genre> getFilmGenres(Long filmId);

    Mpa getRatingById(Integer rId);

    List<Film> getPopularFilms(int count);

    List<Film> getFilmsByDirector(Long directorId, String sortBy);

    List<Film> getCommonFilms(Long userId, Long friendId);

    Integer getLikesCount(Film film);

    List<Director> getDirectorOfTheFilm(Long id);

    void removeFilm(long id);

    List<Film> getPopularFilms(int count, Long genreId, Integer year);
}
