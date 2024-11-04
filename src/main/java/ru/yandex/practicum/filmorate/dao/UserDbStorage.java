package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.inter.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component(value = "userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),             // id
                rs.getString("login"),        // login
                rs.getString("email"),        // email
                rs.getString("name"),         // name
                rs.getDate("birthday").toLocalDate() // birthday
        ));
    }

    @Override
    public User getUserById(Long id) {
        if (id == null) {
            throw new ValidationException("User Id не может быть null");
        }
        User user;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);
        if (userRows.first()) {
            user = new User(
                    userRows.getLong("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
        } else {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден!");
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    public User updateUser(User updatedUser){
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
            return updatedUser;
        } else {
            throw new NotFoundException("Пользователь с ID=" + updatedUser.getId() + " не найден!");
        }
    }
}