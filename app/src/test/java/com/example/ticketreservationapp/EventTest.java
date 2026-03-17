package com.example.ticketreservationapp;
import com.google.firebase.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private Event event;
    private Timestamp time;

    @BeforeEach
    void eventSetUp(){
        LocalDateTime testDate = LocalDateTime.of(2026, 4, 18, 7, 45);
        time = new Timestamp(Date.from(testDate.atZone(ZoneId.systemDefault()).toInstant()));
        event = new Event("Art Exhibition", time, "Pierrefonds-Roxboro", "Art");
    }

    @Test
    void testGetTitle(){
        assertEquals("Art Exhibition", event.getTitle());
    }

    @Test
    void testGetLocation(){
        assertEquals("Pierrefonds-Roxboro", event.getLocation());
    }

    @Test
    void testGetCategory(){
        assertEquals("Art", event.getCategory());
    }

    @Test
    void testGetDate(){
        String date = event.getDate();
        assertEquals("Apr 18, 2026", date);
    }

    @Test
    void testGetDateTime(){
        String dateTime = event.getDateTime();
        assertEquals("07:45 AM", dateTime);
    }

}
