package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {
    @Autowired
    private Validator validator;

    private final User user = new User();

    @BeforeEach
    void setUp() {
        user.setName("userName");
        user.setEmail("valid@mail.ru");
        user.setLogin("loginSuccess");
        user.setBirthday(LocalDate.now().minusDays(1));
    }

    @Test
    void shouldFailNullOrBlankOrHasWhiteSpacesLogin() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());

        user.setLogin(null);
        violations = validator.validate(user);
        assertTrue(!violations.isEmpty());
        assertEquals(1, violations.size());

        user.setLogin("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        user.setEmail("invalid3rvrv3r34.ru");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }
}