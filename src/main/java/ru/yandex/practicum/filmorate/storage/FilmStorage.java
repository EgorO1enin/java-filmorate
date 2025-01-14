package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

interface FilmStorage {
    public Film addFilm(Film film);

    public Collection<Film> getFilms();

    public Film updateFilm(Film film);

    Film getFilm(Long id);
}
