package ru.yandex.practicum.filmorate.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class User {
    private Long id;
    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Некорректный email")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может позже сегоднешнего дня")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}
