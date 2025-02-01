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
        Map<String, Object> filmData = Map.of(
                "name", director.getName()
        );
        try {
            long directorId = simpleJdbcInsert.executeAndReturnKey(filmData).longValue();
            director.setId(directorId);
            return director;
        } catch (Exception e) {
            throw new NotFoundException("Ошибка при добавлении Режисера");
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
        if (getDirectorById(id) != null) {
            // Сначала обновляем записи в таблице films, устанавливая director_id в NULL
            String updateQuery = "UPDATE films SET director_id = NULL WHERE director_id = ?;";
            jdbcTemplate.update(updateQuery, id);

            // Затем удаляем запись из таблицы directors
            String deleteQuery = "DELETE FROM directors WHERE id = ?;";
            jdbcTemplate.update(deleteQuery, id);
        } else {
            throw new NotFoundException("Директор с ID=" + id + " не найден!");
        }
    }

}
