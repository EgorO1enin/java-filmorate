package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feed {
    private long eventId;
    private long timestamp;
    private long userId;
    private EventType eventType;
    private OperationEvent operation;
    private long entityId;
}
