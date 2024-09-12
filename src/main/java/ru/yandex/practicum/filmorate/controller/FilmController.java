package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private Long filmId = 1L;

    @GetMapping()
    public Collection<Film> getFilms(){
        return films.values();
    }

    @PostMapping()
    public Film addFilm(@RequestBody @Validated Film film){
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))){
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года;");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года;");
        }
        film.setId(filmId);
        filmId++;
        films.put(film.getId(), film);
        log.info("Фильм добавлен{}", film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody @Validated Film newFilm){
        Film oldfilm = films.get(newFilm.getId());
        newFilm.setId(oldfilm.getId());
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм обновлен{}", newFilm);
        return newFilm;
    }
}
