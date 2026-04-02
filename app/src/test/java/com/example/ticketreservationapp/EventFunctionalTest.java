package com.example.ticketreservationapp;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EventFunctionalTest {


    @Test
    void shouldRemoveCancelledEventsFromList() {
        Event activeEvent = new Event();
        activeEvent.setEventId("1");
        activeEvent.setTitle("Concert");
        activeEvent.setLocation("Montreal");
        activeEvent.setCategory("Music");
        activeEvent.setStatus("active");

        Event cancelledEvent = new Event();
        cancelledEvent.setEventId("2");
        cancelledEvent.setTitle("Party");
        cancelledEvent.setLocation("Toronto");
        cancelledEvent.setCategory("Fun");
        cancelledEvent.setStatus("cancelled");

        List<Event> fullList = Arrays.asList(activeEvent, cancelledEvent);

        List<Event> visibleEvents = new ArrayList<>();
        for (Event e : fullList) {
            if (!"cancelled".equals(e.getStatus())) {
                visibleEvents.add(e);
            }
        }

        assertEquals(1, visibleEvents.size());
        assertEquals("Concert", visibleEvents.get(0).getTitle());
    }
}