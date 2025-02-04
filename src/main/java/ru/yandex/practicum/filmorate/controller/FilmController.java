package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikesService;

import java.util.Collection;
import java.util.List;

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

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    @PostMapping()
    public Film addFilm(@RequestBody @Validated Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/{filmId}/like/{id}")
    public void likeFilm(@PathVariable long filmId, @PathVariable long id) {
        likesService.addLike(filmId, id);
    }

    @DeleteMapping("/{filmId}/like/{id}")
    public void removeLikeFilm(@PathVariable long filmId, @PathVariable long id) {
        likesService.removeLike(filmId, id);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated Film updatedFilm) {
        return filmService.updateFilm(updatedFilm);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable long id) {
        filmService.removeFilm(id);
    }

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(
            @PathVariable Long directorId,
            @RequestParam String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

   /* @GetMapping("/{filmId}/1")
    public List<Director> getDirectorOfTheFilm(@PathVariable Long filmId) {
        return filmService.getDirectorOfTheFilm(filmId);
    }*/
}
