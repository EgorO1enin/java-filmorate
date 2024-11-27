package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikesService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final LikesService likesService;


    @GetMapping
    public Collection<Film> getFilms() {
       return filmService.getFilms();
    }

    @PostMapping()
    public Film addFilm(@RequestBody @Validated Film film) {
        return filmService.addFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        return filmService.getFilm(id);
    }

    @PutMapping("{filmId}/like/{id}")
    public void likeFilm(@PathVariable long filmId, @PathVariable long id) {
        likesService.addLike(filmId, id);
    }

    @DeleteMapping("{filmId}/like/{id}")
    public String removeLikeFilm(@PathVariable long filmId, @PathVariable long id) {
        likesService.removeLike(filmId, id);
        return "like on film with id = " + filmId + " was removed from user with id " + id;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated Film updatedFilm) {
        return filmService.updateFilm(updatedFilm);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);

    }
}
