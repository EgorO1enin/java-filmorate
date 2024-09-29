package ru.yandex.practicum.filmorate.controller;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.InMemoryFilmStorage;


import java.util.Collection;


@RestController
@RequestMapping("/films")
public class FilmController {


    public final InMemoryFilmStorage inMemoryFilmStorage;
    public final FilmService filmService;


    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }
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

    @PutMapping("{film_id}/like/{id}")
    public Film likeFilm(@PathVariable long film_id, @PathVariable long id) {
        return filmService.likeFilm(film_id, id);
    }

    @DeleteMapping("{film_id}/like/{id}")
    public String removeLikeFilm(@PathVariable long film_id, @PathVariable long id) {
        return filmService.deleteLike(film_id, id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam int count) {
        return filmService.getPopularFilms(count);

    }
}
