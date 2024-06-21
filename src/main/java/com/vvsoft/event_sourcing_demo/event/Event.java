package com.vvsoft.event_sourcing_demo.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vvsoft.event_sourcing_demo.event.reminder.ReminderEvent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(ReminderEvent.class)
})
public interface Event {
}
