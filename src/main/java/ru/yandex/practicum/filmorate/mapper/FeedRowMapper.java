package ru.yandex.practicum.filmorate.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedRowMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEventId(rs.getLong("event_id"));
        feed.setTimeStamp(rs.getTimestamp("timestamp").toLocalDateTime());
        feed.setUserId(rs.getLong("user_id"));
        feed.setEventType(EventType.valueOf(rs.getString("event_type")));
        feed.setOperation(OperationEvent.valueOf(rs.getString("operation")));
        feed.setEntityId(rs.getLong("entity_id"));
        return feed;
    }
}
