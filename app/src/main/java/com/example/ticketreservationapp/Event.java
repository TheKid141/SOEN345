package com.example.ticketreservationapp;

import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {
    private String Title;
    private Timestamp Date;
    private String Location;
    private String Category;

    public Event() {}

    public Event(String title, Timestamp date, String location, String category) {
        this.Title = title;
        this.Date = date;
        this.Location = location;
        this.Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public Timestamp getTimestamp() {
        return Date;
    }

    public Date getRawDate() {
        return Date != null ? Date.toDate() : null;
    }

    public String getDate() {
        return formatDate(Date, "MMM dd, yyyy");
    }

    public String getDateTime(){
        return formatDate(Date, "hh:mm a");
    }

    public String getLocation() {
        return Location;
    }

    public String getCategory() {
        return Category;
    }

    public String formatDate(Timestamp date, String type){
        if(date == null){
            return "";
        }
        SimpleDateFormat simpleDate = new SimpleDateFormat(type, Locale.ENGLISH);
        return simpleDate.format(date.toDate());
    }
}
