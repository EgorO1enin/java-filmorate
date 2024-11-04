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

    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user == null || friend == null) {
            throw new ValidationException("Пользователь с ID=" + userId + " не найден! или пользователь с ID=" + friendId + " не найден!");
        }
        String checkFriendshipQuery = "SELECT COUNT(*) FROM friends WHERE user1 = ? AND user2 = ?";
        Integer countCheckUsersFriends = jdbcTemplate.queryForObject(checkFriendshipQuery, new Object[]{userId, friendId}, Integer.class);
        if (countCheckUsersFriends != null && countCheckUsersFriends > 0) {
            throw new ValidationException("Пользователь с ID=" + userId + " уже является другом пользователя с ID=" + friendId);
        }

        jdbcTemplate.update("INSERT INTO friends (user1, user2) VALUES (?, ?)", userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM friends f " +
                "INNER JOIN users u ON f.user2 = u.id " +
                "WHERE f.user1 = ?";
        if (user.getId() == null){
            throw new ValidationException("Пользователь с ID=" + userId + " не найден!");
        }
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> new User(
                  rs.getLong("id"),
                  rs.getString("email"),
                  rs.getString("login"),
                  rs.getString("name"),
                  rs.getDate("birthday") != null ? rs.getDate("birthday").toLocalDate() : null
          ));

    }

    public User removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        if (user == null || friendId == null) {
            throw new ValidationException("Пользователь с ID=" + userId + " не найден! или пользователь с ID=" + friendId + " не найден!");
        }

        jdbcTemplate.update("DELETE FROM friends WHERE user1 = ? AND user2 = ?", userId, friendId);
        return user;
    }

    public Collection<User> getCommonFriends(Long firstUserId, Long lastUserId) {
        User firstUser = getUserById(firstUserId);
        User lastUser = getUserById(lastUserId);
        if (firstUser == null || lastUser == null) {
            throw new ValidationException("First User or Second User not found");
        }
        String sql = "SELECT USER2 FROM friends WHERE user1 = ?;";
        List<Long> friendsIds = jdbcTemplate.queryForList(sql, new Object[]{firstUserId}, Long.class);
        String sqlFirst = "SELECT user2 FROM friends WHERE user1 = ?";
        List<Long> firstUserFriendsIds = jdbcTemplate.queryForList(sqlFirst, new Object[]{firstUserId}, Long.class);
        String sqlSecond = "SELECT user2 FROM friends WHERE user1 = ?";
        List<Long> secondUserFriendsIds = jdbcTemplate.queryForList(sqlSecond, new Object[]{lastUserId}, Long.class);
        firstUserFriendsIds.retainAll(secondUserFriendsIds);
        List<User> commonFriends = new ArrayList<>();
        for (Long friendId : firstUserFriendsIds) {
            commonFriends.add(getUserById(friendId));
        }
        return commonFriends;
    }
}