package com.example.ticketreservationapp;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityAcceptanceTest {

    @Before
    public void setUp() { Intents.init(); }

    @After
    public void tearDown() { Intents.release(); }

    @Test
    public void testSuccessfulRegistrationNavigatesToEventList() {
        ActivityScenario.launch(RegisterActivity.class);
        String randomEmail = "testuser_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        onView(withId(R.id.emailEditText)).perform(replaceText(randomEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(replaceText("validPassword123"), closeSoftKeyboard());
        onView(withId(R.id.registerEmailButton)).perform(click());
        EspressoUtils.waitForView(withId(R.id.recyclerView), 5000);
        intended(hasComponent(EventListActivity.class.getName()));
    }

    @Test
    public void testBackButtonReturnsToLogin() {
        ActivityScenario<RegisterActivity> scenario = ActivityScenario.launch(RegisterActivity.class);
        onView(withId(R.id.btnBackToLogin)).perform(click());
        assertEquals(Lifecycle.State.DESTROYED, scenario.getState());
    }
}