package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    public Collection<User> getCommonFriends(Long firstUserId, Long lastUserId) {
        User firstUser = userService.getUserById(firstUserId);
        User lastUser = userService.getUserById(lastUserId);
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
            commonFriends.add(userService.getUserById(friendId));
        }
        return commonFriends;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userService.getUserById(userId);
        if (user == null || friendId == null) {
            throw new ValidationException("Пользователь с ID=" + userId + " не найден! или пользователь с ID=" + friendId + " не найден!");
        }

        jdbcTemplate.update("DELETE FROM friends WHERE user1 = ? AND user2 = ?", userId, friendId);
        return user;
    }

    public List<User> getFriends(Long userId) {
        User user = userService.getUserById(userId);
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

    public void addFriend(Long userId, Long friendId) {
        User user = userService.getUserById(userId);
        User friend = userService.getUserById(friendId);
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
}
