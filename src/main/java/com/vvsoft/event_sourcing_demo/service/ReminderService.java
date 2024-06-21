package com.vvsoft.event_sourcing_demo.service;

import com.vvsoft.event_sourcing_demo.entity.Reminder;
import com.vvsoft.event_sourcing_demo.entity.repo.ReminderRepo;
import com.vvsoft.event_sourcing_demo.event.EventWrapper;
import com.vvsoft.event_sourcing_demo.event.event_repo.EventRepository;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent.AddReminderEvent;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent.RescheduleReminderEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReminderService {
    private final EventRepository<ReminderEvent> eventRepo;
    private final ReminderRepo repo;
    private ApplicationEventPublisher eventPublisher;

    public ReminderService(EventRepository<ReminderEvent> eventRepo, ReminderRepo repo, ApplicationEventPublisher applicationEventPublisher) {
        this.eventRepo = eventRepo;
        this.repo = repo;
        this.eventPublisher = applicationEventPublisher;
    }

    public Reminder createReminder(String id, String title, LocalDateTime scheduledAt){
        List<EventWrapper<ReminderEvent>> existingEvents = eventRepo.read(id);
        Reminder reminder = repo.constructState(existingEvents);
        AddReminderEvent addReminderEvent = new AddReminderEvent(id,title, scheduledAt);
        eventRepo.save(addReminderEvent, id, "Reminder", 1);
        reminder.applyEvent(addReminderEvent);
        eventPublisher.publishEvent(addReminderEvent);
        return reminder;
    }

    public Reminder cancelReminder(String id) {
        List<EventWrapper<ReminderEvent>> existingEvents = eventRepo.read(id);
        Reminder reminder = repo.constructState(existingEvents);
        int nextVersion = existingEvents.size() + 1;
        if(nextVersion > 1) {
            ReminderEvent.CancelReminderEvent event = new ReminderEvent.CancelReminderEvent(id);
            eventRepo.save(event,id,"Reminder",nextVersion);
            reminder.applyEvent(event);
            eventPublisher.publishEvent(event);
        }
        else
            throw new IllegalStateException("Reminder doesn't exist : " + id);

        return reminder;
    }

    public Reminder rescheduleReminder(String id, LocalDateTime scheduleAt) {
        List<EventWrapper<ReminderEvent>> events = eventRepo.read(id);
        Reminder reminder = repo.constructState(events);
        if(reminder != null) {
            RescheduleReminderEvent event = new RescheduleReminderEvent(id, scheduleAt);
            eventRepo.save(event, id, "Reminder", events.size() + 1);
            reminder.applyEvent(event);
            eventPublisher.publishEvent(event);
        }
        return reminder;
    }
}
