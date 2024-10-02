package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма должно быть не более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 1, message = "Количество минут фильма должно быть больше нуля")
    private int duration;
    private final Set<Long> likes = new HashSet<>();

    public int getLikesLength() {
        return likes.size();
    }
}
