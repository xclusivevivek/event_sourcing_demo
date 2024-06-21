package com.vvsoft.event_sourcing_demo.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventWrapper<T extends Event> implements Event{
    private String id;
    private String streamId;
    private int version;
    private LocalDateTime createdAt;
    private T event;
}
