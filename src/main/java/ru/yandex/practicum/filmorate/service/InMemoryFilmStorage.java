package ru.yandex.practicum.filmorate.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private Long filmId = 1L;

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года;");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года;");
        }
        film.setId(filmId);
        filmId++;
        films.put(film.getId(), film);
        log.info("Фильм добавлен{}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        if (!films.containsKey(updatedFilm.getId())) {
            throw new NotFoundException("Фильм с id" + updatedFilm.getId() + "не найден");
        }
        Film oldfilm = films.get(updatedFilm.getId());
        updatedFilm.setId(oldfilm.getId());
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Фильм обновлен{}", updatedFilm);
        return updatedFilm;
    }

    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }
}
