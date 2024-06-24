package com.vvsoft.event_sourcing_demo.views;

import com.vvsoft.event_sourcing_demo.entity.Reminder;
import com.vvsoft.event_sourcing_demo.event.Event;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ReminderLoggerView {
    private final Logger logger = LoggerFactory.getLogger(ReminderLoggerView.class);
    private final ConcurrentMap<String, Reminder> stateMap = new ConcurrentHashMap<>();

    @EventListener
    public List<Event> handleEvent(ReminderEvent.AddReminderEvent event) {
        Reminder reminder = new Reminder();
        stateMap.computeIfAbsent(event.id(), id -> {
            reminder.applyEvent(event);
            return reminder;
        });
        printReminder(reminder, "Added");
        return List.of();
    }

    @EventListener
    public List<Event> handleEvent(ReminderEvent.RescheduleReminderEvent event) {
        Reminder currReminder = stateMap.computeIfPresent(event.id(), (id, reminder) -> {
            reminder.applyEvent(event);
            return reminder;
        });
        if(currReminder != null)
            printReminder(currReminder,"Rescheduled");
        return List.of();
    }

    @EventListener
    public List<Event> handleEvent(ReminderEvent.CancelReminderEvent event) {
        Reminder currReminder = stateMap.computeIfPresent(event.id(), (id, reminder) -> {
            reminder.applyEvent(event);
            return reminder;
        });
        if(currReminder != null)
            printReminder(currReminder,"Cancelled");
        return List.of();
    }

    private void printReminder(Reminder reminder, String action) {
        logger.info("A reminder has been {} : \n{}",action,reminder);
    }
}
