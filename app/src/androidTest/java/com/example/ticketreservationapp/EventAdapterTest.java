package com.example.ticketreservationapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.firebase.Timestamp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.time.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EventAdapterTest {

    private EventAdapter adapter;
    private List<Event> events;

    @Before
    public void eventListSetUp(){
        LocalDateTime testDate = LocalDateTime.of(2026, 4, 18, 7, 45);
        Timestamp time1 = new Timestamp(Date.from(testDate.atZone(ZoneId.systemDefault()).toInstant()));

        Event event1 = new Event("Music Festival", time1, "DDO", "Music");
        events = Arrays.asList(event1);
        adapter = new EventAdapter(false, null, null);
        adapter.submitList(events);
    }

    @Test
    public void getCount(){
        assertEquals(1, adapter.getItemCount());
    }
}