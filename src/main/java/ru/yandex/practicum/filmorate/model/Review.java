package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Long reviewId;

    @NotBlank(message = "Отзыв не может быть пустым")
    private String content;

    @NotNull
    private Boolean isPositive;

    private Long userId;

    private Long filmId;

    private Long useful;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("content", this.content);
        map.put("is_positive", this.isPositive);
        map.put("user_id", this.userId);
        map.put("film_id", this.filmId);
        map.put("useful", this.useful);
        return map;
    }
}