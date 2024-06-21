package com.vvsoft.event_sourcing_demo.views;

import com.vvsoft.event_sourcing_demo.event.Event;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent.ReminderEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderInformerView implements ReminderEventHandler {
    private final Logger logger = LoggerFactory.getLogger(ReminderInformerView.class);

    @Override
    @EventListener
    public List<Event> handleEvent(ReminderEvent.AddReminderEvent event) {
        logger.info("A reminder has been added : {}", event.title());
        return List.of();
    }

    @Override
    @EventListener
    public List<Event> handleEvent(ReminderEvent.RescheduleReminderEvent event) {
        logger.info("A reminder has been rescheduled : {}", event.id());
        return List.of();
    }

    @Override
    @EventListener
    public List<Event> handleEvent(ReminderEvent.CancelReminderEvent event) {
        logger.info("A reminder has been cancelled : {}", event.id());
        return List.of();
    }
}
