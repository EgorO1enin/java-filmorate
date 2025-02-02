package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feed {
    private long eventId;
    private LocalDateTime timeStamp;
    private long userId;
    private EventType eventType;
    private OperationEvent  operation;
    private long entityId;
}
