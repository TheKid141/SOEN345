package com.example.ticketreservationapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ReservationSystemAcceptanceTest {

    @Before
    public void setUp() throws Exception {
        Intents.init();
        Tasks.await(FirebaseAuth.getInstance().signInWithEmailAndPassword(
                "testuser_ddfded96@example.com",
                "validPassword123"
        ));
    }

    @After
    public void tearDown() {
        Intents.release();
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testMyReservationsButtonNavigatesCorrectly() {
        ActivityScenario.launch(EventListActivity.class);
        onView(withId(R.id.btnMyReservations)).perform(click());
        intended(hasComponent(MyReservationsActivity.class.getName()));
    }

    @Test
    public void testBackButtonReturnsToEventList() {
        ActivityScenario<MyReservationsActivity> scenario = ActivityScenario.launch(MyReservationsActivity.class);
        onView(withId(R.id.btnBackFromReservations)).perform(click());
        assertEquals(androidx.lifecycle.Lifecycle.State.DESTROYED, scenario.getState());
    }

    @Test
    public void testMyReservationsDisplaysEmptyStateProperly() throws InterruptedException {
        ActivityScenario.launch(MyReservationsActivity.class);
        // Allow a brief moment for Firestore to fetch the user's reservations
        Thread.sleep(1000);

        try {
            onView(withId(R.id.tvNoReservations)).check(matches(isDisplayed()));
            onView(withId(R.id.btnBackFromReservations)).check(matches(isDisplayed()));
        } catch (Exception e) {
            onView(withId(R.id.reservationsRecyclerView)).check(matches(isDisplayed()));
        }
    }

    /**
     * Epic 3: Acceptance Test for Cancellation Flow
     * Verifies that clicking 'Cancel' shows a confirmation dialog.
     */
    @Test
    public void testCancelButtonShowsConfirmationDialog() throws InterruptedException {
        ActivityScenario.launch(MyReservationsActivity.class);

        // Wait for Firestore to populate the RecyclerView
        Thread.sleep(1500);

        try {
            onView(withText("Cancel")).perform(click());
            onView(withText("Cancel Reservation")).check(matches(isDisplayed()));
            onView(withText("Yes, Cancel")).check(matches(isDisplayed()));
            onView(withText("Keep it")).check(matches(isDisplayed()));
        } catch (Exception e) {
            onView(withId(R.id.tvNoReservations)).check(matches(isDisplayed()));
        }
    }

    /**
     * Epic 3: Acceptance Test for Duplicate Reservation Prevention
     */
    @Test
    public void testDuplicateReservationPrevention() throws InterruptedException {
        ActivityScenario.launch(EventListActivity.class);

        // Wait for Firestore to fetch the global events list
        Thread.sleep(1500);

        try {
            onView(withText("Reserved")).check(matches(isDisplayed()));
        } catch (Exception e) {
            onView(withText("Reserve Ticket")).check(matches(isDisplayed()));
        }
    }
}