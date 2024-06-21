package com.vvsoft.event_sourcing_demo;

import com.vvsoft.event_sourcing_demo.entity.repo.ReminderRepo;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;
import com.vvsoft.event_sourcing_demo.event.event_repo.EventRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;

@SpringBootApplication
public class EventSourcingDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext appContext = SpringApplication.run(EventSourcingDemoApplication.class, args);
		var eventRepo =  (EventRepository<ReminderEvent>)appContext.getBean("addReminderEventRepository");
		//var testEvent = new ReminderEvent.AddReminderEvent("Do this", LocalDateTime.of(2024,12,1,6,0));
		var testEvent = new ReminderEvent.CancelReminderEvent("reminderForDec");
		eventRepo.save(testEvent, "reminderForDec","AddReminderEvent",2);
        //var events = eventRepo.read("36f021b0-6066-4871-b745-a4525f20a633");
		ReminderRepo reminderRepo = appContext.getBean(ReminderRepo.class);
		System.out.println(reminderRepo.findReminder("reminderForDec"));
	}



}
