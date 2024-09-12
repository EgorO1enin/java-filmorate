package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmTest {
    @Autowired
    private Validator validator;

    private final Film film = new Film();

    @BeforeEach
    void setUp() {
        film.setName("testName");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("description");
        film.setDuration(100);
    }

    @Test
    void shouldBeFalseWhenNameIsEmpty() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
        film.setName("");
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldBeFalseWhenDescriptionHasMore200Symbols() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
        film.setDescription("description".repeat(21));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void shouldBeFalseWhenDurationIsLessThan1() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
        film.setDuration(0);
        violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}