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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
    public void testInvalidLoginShowsErrors() {
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.loginEmailEditText)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordEditText)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.loginPasswordEditText)).check(matches(hasErrorText("Password must be at least 6 characters")));
    }
    
    @Test
    public void testAdminRoleIsBlocked() {
        ActivityScenario.launch(LoginActivity.class);
        
        onView(withId(R.id.radioAdmin)).perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        
        assert(Intents.getIntents().isEmpty());
    }
}