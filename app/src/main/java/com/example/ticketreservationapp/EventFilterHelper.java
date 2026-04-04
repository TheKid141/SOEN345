package com.example.ticketreservationapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventFilterHelper {

    public List<Event> filterEvents(List<Event> fullList, Calendar selectedCalendar, String selectedCategory, String selectedLocation) {
        List<Event> filteredList = new ArrayList<>();

        for (Event event : fullList) {
            boolean matchesDate = true;
            boolean matchesCategory = true;
            boolean matchesLocation = true;

            if (selectedCalendar != null && event.getRawDate() != null) {
                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(event.getRawDate());
                matchesDate = (eventCal.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                        eventCal.get(Calendar.DAY_OF_YEAR) == selectedCalendar.get(Calendar.DAY_OF_YEAR));
            }

            if (selectedCategory != null && !selectedCategory.equals("All")) {
                matchesCategory = selectedCategory.equalsIgnoreCase(event.getCategory());
            }

            if (selectedLocation != null && !selectedLocation.equals("All")) {
                matchesLocation = selectedLocation.equalsIgnoreCase(event.getLocation());
            }

            if (matchesDate && matchesCategory && matchesLocation) {
                filteredList.add(event);
            }
        }
        return filteredList;
    }
}