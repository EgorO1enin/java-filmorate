//package ru.yandex.practicum.filmorate.controller;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.aot.DisabledInAotMode;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.yandex.practicum.filmorate.service.FilmService;
//import ru.yandex.practicum.filmorate.service.LikesService;
//
//@ContextConfiguration(classes = {FilmController.class})
//@ExtendWith(SpringExtension.class)
//@DisabledInAotMode
//class FilmControllerTest {
//    @Autowired
//    private FilmController filmController;
//
//    @MockBean
//    private FilmService filmService;
//
//    @MockBean
//    private LikesService likesService;
//
//    @DisplayName("Test removeLikeFilm(long, long)")
//    @Disabled("TODO: Complete this test")
//    void testRemoveLikeFilm() throws Exception {
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/films/{filmId}/like/{id}", 1L, 1L);
//        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
//    }
//
//    @Test
//    @DisplayName("Test likeFilm(long, long)")
//    @Disabled("TODO: Complete this test")
//    void testLikeFilm() throws Exception {
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/films/{filmId}/like/{id}", 1L, 1L);
//        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
//    }
//
//    @Test
//    @DisplayName("Test getPopularFilms(int)")
//    @Disabled("TODO: Complete this test")
//    void testGetPopularFilms() throws Exception {
//        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/films/popular");
//        MockHttpServletRequestBuilder requestBuilder = getResult.param("count", String.valueOf(1));
//        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
//    }
//
//    @Test
//    @DisplayName("Test getFilms()")
//    @Disabled("TODO: Complete this test")
//    void testGetFilms() throws Exception {
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/films");
//        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
//    }
//
//    @Test
//    @DisplayName("Test getFilmById(Long)")
//    @Disabled("TODO: Complete this test")
//    void testGetFilmById() throws Exception {
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/films/{id}", 1L);
//        MockMvcBuilders.standaloneSetup(filmController).build().perform(requestBuilder);
//    }
//}
