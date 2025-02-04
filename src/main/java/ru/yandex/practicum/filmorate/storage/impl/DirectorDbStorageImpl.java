package ru.yandex.practicum.filmorate.storage.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

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

    @Transactional
    public Director addDirector(Director director) {
        if (director.getName() == null || director.getName().isEmpty() || director.getName().isBlank()) {
            throw new ValidationException("Имя режиссера не может быть пустым!");
        }

        // Получаем максимальный существующий ID
        String sqlGetMaxId = "SELECT COALESCE(MAX(id), 0) + 1 FROM directors";
        Long newId = jdbcTemplate.queryForObject(sqlGetMaxId, Long.class);

        // Добавляем запись с новым ID
        String sqlInsert = "INSERT INTO directors (id, name) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsert, newId, director.getName());

        director.setId(newId);
        return director;
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
}
