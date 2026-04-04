package com.example.ticketreservationapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class EventListActivityTest {

    @Rule
    public ActivityScenarioRule<EventListActivity> activityRule = new ActivityScenarioRule<>(EventListActivity.class);

    @Test
    public void recyclerViewIsDisplayed() {
        activityRule.getScenario().onActivity(activity -> {
            activity.getWindow().getDecorView().requestFocus();
        });
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void testLogoutButton() {
        ActivityScenario.launch(EventListActivity.class);
        onView(withId(R.id.btnLogout)).perform(click());
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testFilterDialogOpensAndClears() {
        ActivityScenario.launch(EventListActivity.class);
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.spinnerCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerLocation)).check(matches(isDisplayed()));
        onView(withId(R.id.btnClearFilters)).perform(click());
        onView(withId(R.id.btnPickDate)).check(matches(withText("Select Date")));
    }
}