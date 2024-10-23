package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film addFilm(Film film);

    public Collection<Film> getFilms();

    public Film updateFilm(Film film);

    Film getFilm(long id);
}
