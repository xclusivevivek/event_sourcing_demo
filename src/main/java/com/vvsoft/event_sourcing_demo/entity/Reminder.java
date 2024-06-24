package com.vvsoft.event_sourcing_demo.entity;

import com.vvsoft.event_sourcing_demo.event.Event;
import static com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Getter
@Setter
public class Reminder implements ReminderEventHandler {
    private String id;
    private String title;
    private LocalDateTime scheduledAt;
    private ReminderStatus status;

    public void applyEvent(AddReminderEvent event){
        if(status != null)
            throw new IllegalStateException("Invalid Status.Reminder already exists");
        if(event.scheduledAt() == null || event.scheduledAt().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Reminder should be schedule for future");

        title = event.title();
        scheduledAt = event.scheduledAt();
        status = ReminderStatus.SCHEDULED;
        id = event.id();
    }

    public void applyEvent(RescheduleReminderEvent event){
        if(status != ReminderStatus.SCHEDULED)
            throw new IllegalStateException("Invalid Status.Reminder should be already scheduled");
        if(event.scheduledAt() == null || event.scheduledAt().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Reminder should be schedule for future");

        scheduledAt = event.scheduledAt();
        status = ReminderStatus.SCHEDULED;
        id = event.id();
    }

    public void applyEvent(CancelReminderEvent event){
        if(status == null)
            throw new IllegalStateException("Invalid Status.Reminder does not exists");

        status = ReminderStatus.CANCELLED;
        id = event.id();
    }

    @Override
    public List<Event> handleEvent(AddReminderEvent event) {
        applyEvent(event);
        return List.of();
    }

    @Override
    public List<Event> handleEvent(RescheduleReminderEvent event) {
        applyEvent(event);
        return List.of();
    }

    @Override
    public List<Event> handleEvent(CancelReminderEvent event) {
        applyEvent(event);
        return List.of();
    }

    @Override
    public String toString() {
        UnaryOperator<String> resize = input -> String.format("%-15s", input);
        return  String.format("%s:%s\n%s:%s\n%s:%s\n%s:%s",
                resize.apply("Id"),
                this.getId(),
                resize.apply("Title"),
                this.getTitle(),
                resize.apply("ScheduledAt"),
                this.getScheduledAt(),
                resize.apply("Status"),
                this.getStatus());
    }
}
