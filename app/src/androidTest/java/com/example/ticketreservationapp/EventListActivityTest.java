package com.example.ticketreservationapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
}