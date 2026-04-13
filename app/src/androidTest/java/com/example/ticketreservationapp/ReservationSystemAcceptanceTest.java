package com.example.ticketreservationapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
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
import static org.junit.Assert.assertEquals;

import android.content.Intent;

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
        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                MyReservationsActivity.class);
        intent.putExtra("USER_ID", "test-uid-123");
        try (ActivityScenario<MyReservationsActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.btnBackFromReservations)).perform(click());
            scenario.onActivity(activity -> org.junit.Assert.assertTrue(activity.isFinishing()));
        }
    }
    @Test
    public void testMyReservationsUIFrameworkLoads() {
        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                MyReservationsActivity.class);
        intent.putExtra("USER_ID", FirebaseAuth.getInstance()
                .getCurrentUser().getUid());
        ActivityScenario.launch(intent);
        EspressoUtils.waitForView(withId(R.id.btnBackFromReservations), 5000);
        onView(withId(R.id.btnBackFromReservations)).check(matches(isDisplayed()));
    }
}