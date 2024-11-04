/*
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dao.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmControllerTest {

    @InjectMocks
    private FilmController filmController;

    @Mock
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Mock
    private FilmService filmService;

    private Film film;

    @BeforeEach
    void testSetUp() {
        MockitoAnnotations.openMocks(this);
        film = new Film();
        film.setId(1L);
        film.setName("Test Film");
        film.setDescription("A description of the test film.");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
    }

    @Test
    void testGetFilms() {
        when(inMemoryFilmStorage.getFilms()).thenReturn(Arrays.asList(film));

        Collection<Film> films = filmController.getFilms();

        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(film, films.iterator().next());
        verify(inMemoryFilmStorage, times(1)).getFilms();
    }

    @Test
    void testAddFilm() {
        when(inMemoryFilmStorage.addFilm(any(Film.class))).thenReturn(film);

        Film createdFilm = filmController.addFilm(film);

        assertNotNull(createdFilm);
        assertEquals(film.getId(), createdFilm.getId());
        verify(inMemoryFilmStorage, times(1)).addFilm(film);
    }

    @Test
    void testUpdateFilm() {
        when(inMemoryFilmStorage.updateFilm(any(Film.class))).thenReturn(film);

        Film updatedFilm = filmController.updateFilm(film);

        assertNotNull(updatedFilm);
        assertEquals(film.getId(), updatedFilm.getId());
        verify(inMemoryFilmStorage, times(1)).updateFilm(film);
    }

    @Test
    void testLikeFilm() {
        when(filmService.likeFilm(1L, 2L)).thenReturn(film);

        Film likedFilm = filmController.likeFilm(1L, 2L);

        assertNotNull(likedFilm);
        assertEquals(film.getId(), likedFilm.getId());
        verify(filmService, times(1)).likeFilm(1L, 2L);
    }

    @Test
    void testRemoveLikeFilm() {
        when(filmService.deleteLike(1L, 2L)).thenReturn("Like removed");

        String response = filmController.removeLikeFilm(1L, 2L);

        assertEquals("Like removed", response);
        verify(filmService, times(1)).deleteLike(1L, 2L);
    }

    @Test
    void testGetPopularFilms() {
        when(filmService.getPopularFilms(5)).thenReturn(Arrays.asList(film));

        Collection<Film> popularFilms = filmController.getPopularFilms(5);

        assertNotNull(popularFilms);
        assertEquals(1, popularFilms.size());
        verify(filmService, times(1)).getPopularFilms(5);
    }
}*/
