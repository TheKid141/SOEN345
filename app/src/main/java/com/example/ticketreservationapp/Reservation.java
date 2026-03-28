package com.example.ticketreservationapp;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Reservation {
    private String reservationId;
    private String userId;
    private String eventId;
    private String eventTitle;
    private Timestamp eventDate;
    private String eventLocation;
    private String eventCategory;
    private Timestamp reservedAt;
    private String status;

    public Reservation() {}

    public Reservation(String userId, String eventId, String eventTitle, Timestamp eventDate, String eventLocation, String eventCategory) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventCategory = eventCategory;
        this.reservedAt = Timestamp.now();
        this.status = "active";
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    @Exclude
    public Date getRawDate() {
        return eventDate != null ? eventDate.toDate() : null;
    }

    @Exclude
    public String getDate() {
        return formatDate(eventDate, "MMM dd, yyyy");
    }

    @Exclude
    public String getDateTime(){
        return formatDate(eventDate, "hh:mm a");
    }
    public String formatDate(Timestamp date, String type){
        if(date == null){
            return "";
        }
        SimpleDateFormat simpleDate = new SimpleDateFormat(type, Locale.ENGLISH);
        return simpleDate.format(date.toDate());
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventCategory() {
        return eventCategory;
    }
    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Timestamp getReservedAt() {
        return reservedAt;
    }
    public void setReservedAt(Timestamp reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

