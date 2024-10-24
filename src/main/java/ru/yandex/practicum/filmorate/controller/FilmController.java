package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.InMemoryFilmStorage;


import java.util.Collection;


@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {


    public final InMemoryFilmStorage inMemoryFilmStorage;
    public final FilmService filmService;

    @GetMapping()
    public Collection<Film> getFilms() {
       return inMemoryFilmStorage.getFilms();
    }

    @PostMapping()
    public Film addFilm(@RequestBody @Validated Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody @Validated Film updatedFilm) {
       return inMemoryFilmStorage.updateFilm(updatedFilm);
    }

    @PutMapping("{filmId}/like/{id}")
    public Film likeFilm(@PathVariable long filmId, @PathVariable long id) {
        return filmService.likeFilm(filmId, id);
    }

    @DeleteMapping("{filmId}/like/{id}")
    public String removeLikeFilm(@PathVariable long filmId, @PathVariable long id) {
        return filmService.deleteLike(filmId, id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam int count) {
        return filmService.getPopularFilms(count);

    }
}
