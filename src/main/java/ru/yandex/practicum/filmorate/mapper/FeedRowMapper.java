package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.OperationEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class FeedRowMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEventId(rs.getLong("event_id"));
        LocalDateTime now = rs.getTimestamp("timestamp").toLocalDateTime();
        long timestamp = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        feed.setTimestamp(timestamp);
        feed.setUserId(rs.getLong("user_id"));
        feed.setEventType(EventType.valueOf(rs.getString("event_type")));
        feed.setOperation(OperationEvent.valueOf(rs.getString("operation")));
        feed.setEntityId(rs.getLong("entity_id"));
        return feed;
    }
}
