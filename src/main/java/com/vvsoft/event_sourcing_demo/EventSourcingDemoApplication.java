package com.vvsoft.event_sourcing_demo;

import com.vvsoft.event_sourcing_demo.entity.repo.ReminderRepo;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;
import com.vvsoft.event_sourcing_demo.event.event_repo.EventRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.command.annotation.CommandScan;

import java.time.LocalDateTime;

@SpringBootApplication
@CommandScan
public class EventSourcingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventSourcingDemoApplication.class, args);
	}



}
