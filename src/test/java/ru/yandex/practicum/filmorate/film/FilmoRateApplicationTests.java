package ru.yandex.practicum.filmorate.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorageImpl.class, UserRowMapper.class})
class FilmoRateApplicationTests {

    private final UserDbStorageImpl userStorage;
    //@MockBean
    private final FilmDbStorageImpl filmService;

    @Test
    public void testFindUserById() {
        User user = (userStorage.getUserById(1000000L));

        assertThat(user).hasFieldOrPropertyWithValue("id", 1000000L);
    }

    @Test
    void create() {
        Film film1 = new Film(1L, "Film One", "Description for film one",
                LocalDate.of(2020, 1, 1), 120, new Mpa(1L, "G"),
                Set.of(new Genre(1L, "Комедия")));

        filmService.addFilm(film1);

        assertThat(film1.getId()).isEqualTo(1L);
    }

}