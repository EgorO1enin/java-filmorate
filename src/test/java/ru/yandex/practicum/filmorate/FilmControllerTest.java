package ru.yandex.practicum.filmorate;
 import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.practicum.filmorate.controller.UserController;
 import ru.yandex.practicum.filmorate.model.Film;
 import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@WebMvcTest(UserController .class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUserBirthday() throws Exception {
        Film film = new Film();
        film.setName("test");
        film.setDescription("description");
        film.setDuration(0);
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        String filmJson= objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().is4xxClientError());
    }

}