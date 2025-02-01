package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Component(value = "userDbStorage")
public class UserDbStorageImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserDbStorageImpl(final JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User getUserById(Long id) {
        if (id == null) {
            throw new ValidationException("User Id не может быть null");
        }

        String sql = "SELECT * FROM users WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден!");
        }
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public void updateUser(User updatedUser) {
        if (getUserById(updatedUser.getId()) != null) {
            String sqlQuery = "UPDATE users SET " +
                    "email = ?, " +
                    "login = ?, " +
                    "name = ?, " +
                    "birthday = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sqlQuery,
                    updatedUser.getEmail(),
                    updatedUser.getLogin(),
                    updatedUser.getName(),
                    updatedUser.getBirthday(),
                    updatedUser.getId());
        } else {
            throw new NotFoundException("Пользователь с ID=" + updatedUser.getId() + " не найден!");
        }
    }

    @Override
    public void removeUser(long id) {
        User newUser = getUserById(id);
        if (newUser != null) {
            String sqlQuery = "DELETE FROM users WHERE id = ?";
            jdbcTemplate.update(sqlQuery, id);
        } else {
            throw new NotFoundException("Пользователь с ID = " + id + " не найден!");
        }
    }
}