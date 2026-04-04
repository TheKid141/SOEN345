package com.example.ticketreservationapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AdminE2EAcceptanceTest {

    @Test
    public void testAdminCanCreateNewEventSuccessfully() {
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.radioAdmin)).perform(click());
        onView(withId(R.id.loginEmailEditText)).perform(replaceText("admin"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEditText)).perform(replaceText("admin123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        EspressoUtils.waitForView(withId(R.id.fabAddEvent), 5000);

        onView(withId(R.id.fabAddEvent)).perform(click());

        String uniqueEventName = "Final Exam " + UUID.randomUUID().toString().substring(0, 5);
        onView(withId(R.id.etEventTitle)).perform(replaceText(uniqueEventName), closeSoftKeyboard());
        onView(withId(R.id.etEventLocation)).perform(replaceText("Concordia SGW"), closeSoftKeyboard());
        onView(withId(R.id.etEventCategory)).perform(replaceText("Education"), closeSoftKeyboard());
        onView(withId(R.id.etEventCapacity)).perform(replaceText("100"), closeSoftKeyboard());

        onView(withId(R.id.btnPickEventDate)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btnPickEventTime)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btnSaveEvent)).perform(click());

        EspressoUtils.waitForView(withText(uniqueEventName), 5000);
        onView(withText(uniqueEventName)).check(matches(isDisplayed()));
    }
}