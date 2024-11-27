package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.service.GenreService;

@ContextConfiguration(classes = {GenreController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class GenreControllerDiffblueTest {
    @Autowired
    private GenreController genreController;

    @MockBean
    private GenreDbStorage genreDbStorage;

    @MockBean
    private GenreService genreService;


    @Test
    @DisplayName("Test getAllGenres()")
    @Disabled("TODO: Complete this test")
    void testGetAllGenres() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/genres/{id}", 1L);
        MockMvcBuilders.standaloneSetup(genreController).build().perform(requestBuilder);
    }
}
