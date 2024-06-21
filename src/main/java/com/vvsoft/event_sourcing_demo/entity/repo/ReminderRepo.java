package com.vvsoft.event_sourcing_demo.entity.repo;

import com.vvsoft.event_sourcing_demo.entity.Reminder;
import com.vvsoft.event_sourcing_demo.event.EventWrapper;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;
import com.vvsoft.event_sourcing_demo.event.event_repo.EventRepository;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent.AddReminderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReminderRepo {
    private final Logger logger = LoggerFactory.getLogger(ReminderRepo.class);

    private final EventRepository<ReminderEvent> eventRepo;

    @Autowired
    public ReminderRepo(EventRepository<ReminderEvent> eventRepo) {
        this.eventRepo = eventRepo;

    }

    public Reminder findReminder(String reminderId){
        List<EventWrapper<ReminderEvent>> events = eventRepo.read(reminderId);
        return constructState(events);
    }

    public Reminder constructState(List<EventWrapper<ReminderEvent>> events){
        Reminder reminder = new Reminder();
        events.forEach(event -> {
            ReminderEvent reminderEvent = event.getEvent();
            reminderEvent.acceptHandler(reminder);
        } );
        return reminder;
    }

}
