package com.example.ticketreservationapp;

import android.view.View;
import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public class EspressoUtils {

    /**
     * Safely waits for a view to appear, even across completely new Activity transitions.
     * @param viewMatcher The view you are waiting for
     * @param timeoutMs The maximum amount of time to wait
     */
    public static void waitForView(Matcher<View> viewMatcher, long timeoutMs) {
        long endTime = System.currentTimeMillis() + timeoutMs;

        while (System.currentTimeMillis() < endTime) {
            try {
                onView(viewMatcher).check(matches(isDisplayed()));
                return;
            } catch (Throwable t) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}
            }
        }
        throw new AssertionError("View not found within timeout of " + timeoutMs + " ms");
    }
}