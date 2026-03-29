package com.example.ticketreservationapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.VerificationModes.times;

@RunWith(AndroidJUnit4.class)
public class LoginActivityAcceptanceTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void emptyPassword(){
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.loginEmailEditText)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.loginPasswordEditText))
                .check(matches(hasErrorText("Please enter a password")));
    }

    @Test
    public void emptyEmail(){
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.loginEmailEditText)).check(matches(hasErrorText("Please enter an email address")));
    }

    @Test
    public void InvalidEmail(){
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.loginEmailEditText)).perform(typeText("notanemail"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.loginEmailEditText)).check(matches(hasErrorText("Please enter a valid email address")));
    }

    @Test
    public void invalidUser() throws InterruptedException {
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.loginEmailEditText)).perform(typeText("invalid@example.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("invalidpw"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(2000);
        intended(hasComponent(EventListActivity.class.getName()), times(0));
    }

    @Test
    public void validUser() throws InterruptedException {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.loginEmailEditText)).perform(replaceText("testuser_ddfded96@example.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEditText)).perform(replaceText("validPassword123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(3000);
        intended(hasComponent(EventListActivity.class.getName()));
    }
}