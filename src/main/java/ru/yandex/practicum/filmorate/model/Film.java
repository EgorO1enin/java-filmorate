package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма должно быть не более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 1, message = "Количество минут фильма должно быть больше нуля")
    private int duration;
}
