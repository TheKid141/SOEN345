package com.example.ticketreservationapp;

import com.google.firebase.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private Event event;
    private Event emptyEvent;
    private Timestamp time;

    @BeforeEach
    void eventSetUp(){
        LocalDateTime testDate = LocalDateTime.of(2026, 4, 18, 7, 45);
        time = new Timestamp(Date.from(testDate.atZone(ZoneId.systemDefault()).toInstant()));
        event = new Event("Art Exhibition", time, "Pierrefonds-Roxboro", "Art");
        emptyEvent = new Event();
    }

    @Test
    void testContructorInitialization() {
        assertEquals("Art Exhibition", event.getTitle());
        assertEquals("Pierrefonds-Roxboro", event.getLocation());
        assertEquals("Art", event.getCategory());
        assertEquals("Apr 18, 2026", event.getFormattedDate());
        assertTrue(event.getDateTime().contains("07:45"));
    }

    @Test
    void testGetRawDateNotNull(){
        assertNotNull(event.getRawDate());
    }

    @Test
    void testCapacityRemainingTickets() {
        event.setCapacity(100);
        event.setTicketsBooked(97);

        assertEquals(3, event.getAvailableTickets());
        assertFalse(event.isSoldOut());

        event.setTicketsBooked(100);

        assertEquals(0, event.getAvailableTickets());
        assertTrue(event.isSoldOut());
    }

    @Test
    void testEmptyEvent() {
        assertNull(emptyEvent.getTitle());
        assertNull(emptyEvent.getRawDate());
        assertEquals("", emptyEvent.getFormattedDate());
    }
}