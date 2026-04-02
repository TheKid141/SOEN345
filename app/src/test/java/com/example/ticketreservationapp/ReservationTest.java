package com.example.ticketreservationapp;

import com.google.firebase.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    private Reservation reservation;
    private Reservation emptyReservation;
    private Timestamp testTime;

    @BeforeEach
    void setUp() {
        LocalDateTime date = LocalDateTime.of(2026, 12, 25, 19, 30);
        testTime = new Timestamp(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));

        reservation = new Reservation("user123", "event456", "Holiday Concert", testTime, "Bell Centre", "Music");
        emptyReservation = new Reservation();
    }

    @Test
    void testConstructorInitialization() {
        assertEquals("user123", reservation.getUserId());
        assertEquals("event456", reservation.getEventId());
        assertEquals("Holiday Concert", reservation.getEventTitle());
        assertEquals("Bell Centre", reservation.getEventLocation());
        assertEquals("Music", reservation.getEventCategory());

        // Ensure new reservations default to active status
        assertEquals("active", reservation.getStatus());
        assertNotNull(reservation.getReservedAt());
    }

    @Test
    void testDateFormatting() {
        assertEquals("Dec 25, 2026", reservation.getDate());
        assertTrue(reservation.getDateTime().contains("07:30")); // Formatting returns 12-hour AM/PM
        assertNotNull(reservation.getRawDate());
    }

    @Test
    void testEmptyConstructorHandlesNullDatesGracefully() {
        assertNull(emptyReservation.getEventDate());
        assertNull(emptyReservation.getRawDate());
        assertEquals("", emptyReservation.getDate(), "Null timestamp should return an empty string for formatted date");
        assertEquals("", emptyReservation.getDateTime(), "Null timestamp should return an empty string for formatted time");
    }

    @Test
    void testSettersAndGetters() {
        emptyReservation.setReservationId("res999");
        emptyReservation.setStatus("cancelled");

        assertEquals("res999", emptyReservation.getReservationId());
        assertEquals("cancelled", emptyReservation.getStatus());
    }
}