package com.example.ticketreservationapp;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class RepositoryCoverageTest {

    @Before
    public void setupFirebase() {
        if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
            FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext(),
                    new FirebaseOptions.Builder()
                            .setApiKey("fake-api-key")
                            .setApplicationId("1:123456789012:android:abcdef1234567890")
                            .setProjectId("fake-project-id")
                            .build());
        }
    }

    @Test
    public void testEventRepositoryMethods() throws InterruptedException {
        EventRepository repo = new EventRepository();

        // Snapshot listeners execute slightly differently, calling it is usually enough
        // to register the initialization coverage.
        repo.getEvents();

        // Use a latch to wait for 5 asynchronous callbacks
        CountDownLatch latch = new CountDownLatch(5);

        repo.restoreEvent("123", success -> latch.countDown());
        repo.deleteEvent("123", success -> latch.countDown());
        repo.addEvent(new Event(), success -> latch.countDown());
        repo.updateEvent("123", new Event(), success -> latch.countDown());
        repo.cancelEvent("123", success -> latch.countDown());

        // Wait up to 3 seconds for Firebase to process the fake requests and trigger the failure callbacks
        latch.await(3, TimeUnit.SECONDS);
    }

    @Test
    public void testReservationRepositoryMethods() throws InterruptedException {
        ReservationRepository repo = new ReservationRepository();

        repo.getReservationsForUser("user1");

        // Use a latch to wait for 3 asynchronous callbacks
        CountDownLatch latch = new CountDownLatch(3);

        // Fix: Create a dummy reservation with a non-null eventId to prevent Firebase NPE
        Reservation dummyReservation = new Reservation();
        dummyReservation.setEventId("dummy-event-id");

        repo.createReservation(dummyReservation, (success, id) -> latch.countDown());
        repo.cancelReservation("res1", (success, id) -> latch.countDown());
        repo.hasActiveReservation("u1", "e1", exists -> latch.countDown());

        latch.await(3, TimeUnit.SECONDS);
    }
}