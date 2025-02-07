package ru.yandex.practicum.filmorate.storage.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DirectorDbStorageImpl implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getDirectors() {
        String sql = "SELECT * FROM directors";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Director(
                rs.getLong("id"),
                rs.getString("name"))
        );
    }

    @Override
    public Director getDirectorById(Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой аргумент");
        }
        Director director;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM directors WHERE id = ?", id);
        if (filmRows.first()) {
            director = new Director(
                    filmRows.getLong("id"),
                    filmRows.getString("name")
            );
        } else {
            throw new NotFoundException("Режисер с ID=" + id + " не найден!");
        }
        return director;
    }


    @Override
    public Director addDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");

        // Подготовка данных для добавления фильма
        Map<String, Object> directorData = new HashMap<>();
        directorData.put("name", director.getName());
        try {
            // Добавление фильма и получение его ID
            long filmId = simpleJdbcInsert.executeAndReturnKey(directorData).longValue();
            director.setId(filmId);
            return director;
        } catch (Exception e) {
            throw new NotFoundException("Ошибка при добавлении режисера : " + e.getMessage());
        }
    }

    @Override
    public Director updateDirector(Director director) {
        if (director == null) {
            throw new ValidationException("Передан устой аргумент");
        }
        String sqlQuery = "UPDATE directors SET " +
                "name = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery,
                director.getName(),
                director.getId()) != 0) {
            return director;
        } else {
            throw new NotFoundException("Фильм с ID=" + director.getId() + " не найден!");
        }
    }

    @Override
    public void deleteDirector(Long id) {
        // Проверяем, существует ли директор с данным ID
        Director director = getDirectorById(id);
        if (director != null) {
            // Затем удаляем запись из таблицы directors
            String deleteQuery = "DELETE FROM directors WHERE id = ?";
            jdbcTemplate.update(deleteQuery, id);
        } else {
            throw new NotFoundException("Директор с ID=" + id + " не найден!");
        }
    }

    @Override
    public void addFilmDirector(Long filmId, Long directorId) {
        String sql = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, directorId);
    }

    @Override
    public void deleteDirectorsByFilmId(Long filmId) {
        String sqlQuery = "DELETE FROM film_directors WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

}
