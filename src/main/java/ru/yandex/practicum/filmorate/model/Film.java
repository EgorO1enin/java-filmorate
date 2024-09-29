package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film implements Comparable<Film> {

    @Override
    public int compareTo(Film o) {
        if (likes.size() == o.getLikes().size()) {
            return 0;
        }
        if (likes.size() > o.getLikes().size()) {
            return 1;
        }
        return -1;
    }

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
