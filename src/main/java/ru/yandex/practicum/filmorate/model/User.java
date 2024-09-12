package ru.yandex.practicum.filmorate.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.time.LocalDate;

@Data
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
}
