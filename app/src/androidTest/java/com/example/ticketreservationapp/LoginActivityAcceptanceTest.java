package com.example.ticketreservationapp;

import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.VerificationModes.times;
import static androidx.test.espresso.action.ViewActions.replaceText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityAcceptanceTest {

    // Add the ActivityScenarioRule to actually launch the LoginActivity
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    // Keep the IntentsRule to manage the Espresso Intents lifecycle
    @Rule
    public IntentsRule intentsRule = new IntentsRule();

    @Test
    public void emptyPassword() throws InterruptedException {
        onView(withId(R.id.loginEmailEditText)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.loginPasswordEditText))
                .check(matches(hasErrorText("Please enter a password")));
    }
    @Test
    public void emptyEmail() throws InterruptedException {
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.loginEmailEditText)).check(matches(hasErrorText("Please enter an email address")));
    }

    @Test
    public void InvalidEmail() throws InterruptedException {
        onView(withId(R.id.loginEmailEditText)).perform(typeText("notanemail"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.loginEmailEditText)).check(matches(hasErrorText("Please enter a valid email address")));
    }

    @Test
    public void invalidUser() throws InterruptedException {
        onView(withId(R.id.loginEmailEditText)).perform(typeText("invalid@example.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("invalidpw"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(3000);
        intended(hasComponent(EventListActivity.class.getName()), times(0));
    }

    @Test
    public void validUser() throws InterruptedException {
        onView(withId(R.id.loginEmailEditText)).perform(replaceText("testuser_ddfded96@example.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEditText)).perform(replaceText("validPassword123"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(3000);
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }
}