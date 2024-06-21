package com.vvsoft.event_sourcing_demo.console;

import com.vvsoft.event_sourcing_demo.entity.Reminder;
import com.vvsoft.event_sourcing_demo.entity.repo.ReminderRepo;
import com.vvsoft.event_sourcing_demo.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Command
public class ReminderManagerConsole{

    private final ReminderService reminderService;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public ReminderManagerConsole(ReminderService reminderService) {
        this.reminderService = reminderService;
    }


    @Command(command = "add_reminder", alias = "addrem")
    public String addReminder(@Option(longNames = "id",shortNames = 'i') String id,@Option(longNames = "title",shortNames = 't') String title,@Option(longNames = "schedule",shortNames = 's') String scheduledAt) throws Exception {
        LocalDateTime scheduledTime = LocalDateTime.parse(scheduledAt, timeFormatter);
        Reminder reminder = reminderService.createReminder(id,title, scheduledTime);
        return String.format("Reminder Created : %s",reminder);
    }

    @Command(command = "cancel_reminder", alias = "cancelrem")
    public String cancelReminder(@Option(longNames = "id",shortNames = 'i') String id) throws Exception {
        Reminder reminder = reminderService.cancelReminder(id);
        return String.format("Reminder Cancelled : %s",reminder);
    }

    @Command(command = "reschedule_reminder", alias = "reschedulerem")
    public String rescheduleReminder(@Option(longNames = "id",shortNames = 'i') String id,@Option(longNames = "schedule",shortNames = 's') String scheduledAt) throws Exception {
        Reminder reminder = reminderService.rescheduleReminder(id,LocalDateTime.parse(scheduledAt,timeFormatter));
        return String.format("Reminder Rescheduled : %s",reminder);
    }
}
