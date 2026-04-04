package com.example.ticketreservationapp;

import static org.junit.jupiter.api.Assertions.*;
import com.google.firebase.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventFilterTest {

    private List<Event> fullList;
    private Event event1, event2, event3;
    private EventFilterHelper filterHelper;

    @BeforeEach
    void setUp() {
        filterHelper = new EventFilterHelper();
        fullList = new ArrayList<>();

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2026, Calendar.OCTOBER, 10);
        event1 = new Event("Music Festival", new Timestamp(cal1.getTime()), "Montreal", "Music");

        Calendar cal2 = Calendar.getInstance();
        cal2.set(2026, Calendar.OCTOBER, 11);
        event2 = new Event("Art Expo", new Timestamp(cal2.getTime()), "Toronto", "Art");

        Calendar cal3 = Calendar.getInstance();
        cal3.set(2026, Calendar.OCTOBER, 10);
        event3 = new Event("Other Music", new Timestamp(cal3.getTime()), "Montreal", "Music");

        fullList.add(event1);
        fullList.add(event2);
        fullList.add(event3);
    }

    @Test
    void testFilterByCategory() {
        List<Event> filtered = filterHelper.filterEvents(fullList, null, "Music", "All");
        assertEquals(2, filtered.size());
    }

    @Test
    void testFilterByLocation() {
        List<Event> filtered = filterHelper.filterEvents(fullList, null, "All", "Toronto");
        assertEquals(1, filtered.size());
        assertEquals("Art Expo", filtered.get(0).getTitle());
    }

    @Test
    void testMultipleFilters() {
        List<Event> filtered = filterHelper.filterEvents(fullList, null, "Music", "Montreal");
        assertEquals(2, filtered.size());
    }

    @Test
    void testClearFiltersReturnsAll() {
        List<Event> filtered = filterHelper.filterEvents(fullList, null, "All", "All");
        assertEquals(3, filtered.size());
    }
}