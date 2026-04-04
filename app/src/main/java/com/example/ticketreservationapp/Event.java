package com.example.ticketreservationapp;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {
    // Standard Java naming conventions (lowercase)
    private String title;
    private Timestamp date;
    private String location;
    private String category;
    private String eventId;
    private String status;
    private int capacity;
    private int ticketsBooked;

    public Event() {}

    public Event(String title, Timestamp date, String location, String category) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.category = category;
        this.status = "active"; // All new events start as active
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    @Exclude
    public Date getRawDate() { return date != null ? date.toDate() : null; }

    @Exclude
    public String getFormattedDate() { return formatDate(date, "MMM dd, yyyy"); }

    @Exclude
    public String getDateTime(){ return formatDate(date, "hh:mm a"); }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Exclude
    public String getEventId() { return eventId; }
    @Exclude
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getTicketsBooked() { return ticketsBooked; }
    public void setTicketsBooked(int ticketsBooked) { this.ticketsBooked = ticketsBooked; }

    @Exclude
    public int getAvailableTickets() {
        if (capacity <= 0) return Integer.MAX_VALUE;
        return Math.max(0, capacity - ticketsBooked);
    }

    @Exclude
    public boolean isSoldOut() {
        return capacity > 0 && ticketsBooked >= capacity;
    }

    @Exclude
    public String formatDate(Timestamp date, String type){
        if(date == null){ return ""; }
        SimpleDateFormat simpleDate = new SimpleDateFormat(type, Locale.ENGLISH);
        return simpleDate.format(date.toDate());
    }
}