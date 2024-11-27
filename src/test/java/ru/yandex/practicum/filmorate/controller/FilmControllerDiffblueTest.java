package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikesService;

import java.time.LocalDate;

@ContextConfiguration(classes = {FilmController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class FilmControllerDiffblueTest {
    @Autowired
    private FilmController filmController;

    @MockBean
    private FilmService filmService;

    @MockBean
    private LikesService likesService;

    @Test
    @DisplayName("Test addFilm(Film)")
    @Disabled("TODO: Complete this test")
    void testAddFilm() throws Exception {
        Film film = new Film();
        film.setDescription("The characteristics of someone or something");
        film.setDuration(1);
        film.setId(1L);
        film.setMpa(new Mpa(1L, "Name"));
        film.setName("Name");
        film.setReleaseDate(LocalDate.of(1970, 1, 1));
        String content = (new ObjectMapper()).writeValueAsString(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
    }


    @Test
    @DisplayName("Test updateFilm(Film)")
    @Disabled("TODO: Complete this test")
    void testUpdateFilm() throws Exception {
        Film film = new Film();
        film.setDescription("The characteristics of someone or something");
        film.setDuration(1);
        film.setId(1L);
        film.setMpa(new Mpa(1L, "Name"));
        film.setName("Name");
        film.setReleaseDate(LocalDate.of(1970, 1, 1));
        String content = (new ObjectMapper()).writeValueAsString(film);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
    }

    @DisplayName("Test removeLikeFilm(long, long)")
    @Disabled("TODO: Complete this test")
    void testRemoveLikeFilm() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/films/{filmId}/like/{id}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test likeFilm(long, long)")
    @Disabled("TODO: Complete this test")
    void testLikeFilm() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/films/{filmId}/like/{id}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test getPopularFilms(int)")
    @Disabled("TODO: Complete this test")
    void testGetPopularFilms() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/films/popular");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("count", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test getFilms()")
    @Disabled("TODO: Complete this test")
    void testGetFilms() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/films");
        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test getFilmById(Long)")
    @Disabled("TODO: Complete this test")
    void testGetFilmById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/films/{id}", 1L);
        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
    }
}
