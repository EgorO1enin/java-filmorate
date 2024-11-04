package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(1L, "email", "login", "ekdfj", LocalDate.now());
        user.setId((long) rs.getInt("ID"));
        user.setName(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        user.setName(rs.getString("NAME"));
        return user;
    }
}