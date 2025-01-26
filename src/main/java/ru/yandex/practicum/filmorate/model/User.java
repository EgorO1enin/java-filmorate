package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", this.email);
        map.put("login", this.login);
        map.put("name", this.name);
        map.put("birthday", this.birthday);
        return map;
    }
}
