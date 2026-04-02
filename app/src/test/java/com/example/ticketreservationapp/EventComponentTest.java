package com.example.ticketreservationapp;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventComponentTest {

    @Test
    void shouldReflectUpdatedEventDetailsInList() {
        Event event = new Event();
        event.setEventId("1");
        event.setTitle("Concert");
        event.setLocation("Montreal");
        event.setCategory("Music");

        event.setTitle("Updated Concert");
        event.setLocation("Toronto");
        event.setCategory("Festival");

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);

        assertEquals("Updated Concert", eventList.get(0).getTitle());
        assertEquals("Toronto", eventList.get(0).getLocation());
        assertEquals("Festival", eventList.get(0).getCategory());
    }
}