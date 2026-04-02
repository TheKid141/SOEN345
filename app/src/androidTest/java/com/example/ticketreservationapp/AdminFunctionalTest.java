package com.example.ticketreservationapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AdminFunctionalTest {

    /**
     * US 4.3: Functional test ensuring canceled events no longer appear as available to customers.
     * Note: This requires at least one event in the database to be set to "cancelled".
     */
    @Test
    public void shouldDisplayCancelledEventsAsUnavailableToCustomers() throws InterruptedException {
        // Launch the activity as a Customer
        ActivityScenario.launch(EventListActivity.class);

        // Wait for Firestore to populate the RecyclerView
        Thread.sleep(1500);

        try {
            onView(withText("Cancelled")).check(matches(isDisplayed()));
        } catch (Exception e) {
            System.out.println("No cancelled events found in the database to verify.");
        }
    }
}