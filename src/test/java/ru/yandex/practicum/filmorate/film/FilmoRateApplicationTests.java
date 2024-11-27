package ru.yandex.practicum.filmorate.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class FilmoRateApplicationTests {

    private final UserDbStorage userStorage;
    //@MockBean
    private final FilmDbStorage filmService;

    @Test
    public void testFindUserById() {
        User user = (userStorage.getUserById(1000000L));

        assertThat(user).hasFieldOrPropertyWithValue("id", 1000000L);
    }

    @Test
    void create() {
        Film film1 = new Film(100000L, "Film One", "Description for film one", LocalDate.of(2020, 1, 1), 120, new Mpa(1L, "G"), Set.of(new Genre(1L, "Комедия")));

        filmService.addFilm(film1);

        assertThat(film1).hasFieldOrPropertyWithValue("id", 1);
    }

}