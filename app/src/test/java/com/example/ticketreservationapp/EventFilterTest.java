package com.example.ticketreservationapp;

import static org.junit.jupiter.api.Assertions.*;
import com.google.firebase.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.*;
import java.util.List;

public class EventFilterTest {

    private List<Event> fullList;
    private Event event1;
    private Event event2;
    private Event event3;

    @BeforeEach
    void setUp() {
        fullList = new ArrayList<>();


        Calendar cal1 = Calendar.getInstance();
        cal1.set(2026, Calendar.OCTOBER, 10);
        event1 = new Event("Music Festival", new Timestamp(cal1.getTime()), "Montreal", "Music");

        Calendar cal2 = Calendar.getInstance();
        cal2.set(2026, Calendar.OCTOBER, 11);
        event2 = new Event("Art Expo", new Timestamp(cal2.getTime()), "Toronto", "Art");

        Calendar cal3 = Calendar.getInstance();
        cal3.set(2026, Calendar.OCTOBER, 10);
        event3 = new Event("Other Music Festival", new Timestamp(cal3.getTime()), "Montreal", "Music");

        fullList.add(event1);
        fullList.add(event2);
        fullList.add(event3);
    }

    @Test
    void testFilterByCategory() {
        String selectedCategory = "Music";
        List<Event> filtered = new ArrayList<>();
        for (Event e : fullList) {
            if (e.getCategory().equalsIgnoreCase(selectedCategory)) {
                filtered.add(e);
            }
        }
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(event1));
        assertTrue(filtered.contains(event3));
        assertFalse(filtered.contains(event2));
    }

    @Test
    void testFilterByLocation() {
        String selectedLocation = "Toronto";
        List<Event> filtered = new ArrayList<>();
        for (Event e : fullList) {
            if (e.getLocation().equalsIgnoreCase(selectedLocation)) {
                filtered.add(e);
            }
        }
        assertEquals(1, filtered.size());
        assertTrue(filtered.contains(event2));
    }

    @Test
    void testFilterByDate() {
        Calendar filterCal = Calendar.getInstance();
        filterCal.set(2026, Calendar.OCTOBER, 10);

        List<Event> filtered = new ArrayList<>();
        for (Event e : fullList) {
            Calendar eventCal = Calendar.getInstance();
            eventCal.setTime(e.getRawDate());
            if (eventCal.get(Calendar.YEAR) == filterCal.get(Calendar.YEAR) &&
                eventCal.get(Calendar.DAY_OF_YEAR) == filterCal.get(Calendar.DAY_OF_YEAR)) {
                filtered.add(e);
            }
        }
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(event1));
        assertTrue(filtered.contains(event3));
    }

    @Test
    void testMultipleFilters() {
        String selectedCategory = "Music";
        String selectedLocation = "Montreal";
        
        List<Event> filtered = new ArrayList<>();
        for (Event e : fullList) {
            boolean matchesCat = e.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesLoc = e.getLocation().equalsIgnoreCase(selectedLocation);
            if (matchesCat && matchesLoc) {
                filtered.add(e);
            }
        }
        assertEquals(2, filtered.size());
    }

    @Test
    void testClearFilters() {
        // Simulating "All" logic
        String selectedCategory = "All";
        String selectedLocation = "All";
        
        List<Event> filtered = new ArrayList<>();
        for (Event e : fullList) {
            boolean matchesCat = selectedCategory.equals("All") || e.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesLoc = selectedLocation.equals("All") || e.getLocation().equalsIgnoreCase(selectedLocation);
            if (matchesCat && matchesLoc) {
                filtered.add(e);
            }
        }
        assertEquals(3, filtered.size());
    }
}
