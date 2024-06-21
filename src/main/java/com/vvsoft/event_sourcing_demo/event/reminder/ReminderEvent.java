package com.vvsoft.event_sourcing_demo.event.reminder;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vvsoft.event_sourcing_demo.event.Event;

import java.time.LocalDateTime;
import java.util.List;

import static com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = AddReminderEvent.class,name = "AddReminderEvent"),
        @JsonSubTypes.Type(value = RescheduleReminderEvent.class,name = "RescheduleReminderEvent"),
        @JsonSubTypes.Type(value = CancelReminderEvent.class,name = "CancelReminderEvent"),
})
public sealed interface ReminderEvent extends Event{

    List<Event> acceptHandler(ReminderEventHandler handler);

    public record AddReminderEvent(String id,String title, LocalDateTime scheduledAt) implements ReminderEvent {
        @Override
        public List<Event> acceptHandler(ReminderEventHandler handler) {
            return handler.handleEvent(this);
        }
    }

    public record RescheduleReminderEvent(String id, LocalDateTime scheduledAt) implements ReminderEvent {
        @Override
        public List<Event> acceptHandler(ReminderEventHandler handler) {
            return handler.handleEvent(this);
        }
    }

    public record CancelReminderEvent(String id) implements ReminderEvent {
        @Override
        public List<Event> acceptHandler(ReminderEventHandler handler) {
            return handler.handleEvent(this);
        }
    }

    public interface ReminderEventHandler {
        List<Event> handleEvent(ReminderEvent.AddReminderEvent event);
        List<Event> handleEvent(ReminderEvent.RescheduleReminderEvent event);
        List<Event> handleEvent(ReminderEvent.CancelReminderEvent event);
    }
}


