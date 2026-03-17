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
    private Timestamp time1;
    private Timestamp time2;

    @Before
    public void eventListSetUp(){
        LocalDateTime testDate = LocalDateTime.of(2026, 4, 18, 7, 45);
        time1 = new Timestamp(Date.from(testDate.atZone(ZoneId.systemDefault()).toInstant()));

        LocalDateTime testDate2 = LocalDateTime.of(2026, 7, 3, 10, 00);
        time2 = new Timestamp(Date.from(testDate2.atZone(ZoneId.systemDefault()).toInstant()));


        Event event1 = new Event("Music Festival", time1, "DDO", "Music");
        Event event2 = new Event("Raptors vs Jazz", time2, "Toronto", "Sports");

        events = Arrays.asList(
            event1, event2
        );

        adapter = new EventAdapter(events);
    }

    @Test
    public void getCount(){
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testEmptyList() {
        EventAdapter emptyAdapter = new EventAdapter(Arrays.asList());
        assertEquals(0, emptyAdapter.getItemCount());
    }

}
