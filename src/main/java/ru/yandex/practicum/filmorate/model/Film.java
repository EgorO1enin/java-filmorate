package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
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
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
    private List<Director> directors;
}