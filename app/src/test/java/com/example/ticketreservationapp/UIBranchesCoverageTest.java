package com.example.ticketreservationapp;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class UIBranchesCoverageTest {

    // THIS IS THE MISSING PIECE!
    @Before
    public void setupFirebase() {
        if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
            FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext(),
                    new FirebaseOptions.Builder()
                            .setApiKey("fake-api-key")
                            .setApplicationId("1:123456789012:android:abcdef1234567890")
                            .setProjectId("fake-project-id")
                            .build());
        }
    }

    @Test
    public void testEventListAdminDialogs() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventListActivity.class);
        intent.putExtra("IS_ADMIN", true);

        try (ActivityScenario<EventListActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                RecyclerView rv = activity.findViewById(R.id.recyclerView);
                EventAdapter adapter = (EventAdapter) rv.getAdapter();

                // Force an item into the adapter so we can click it
                Event fakeEvent = new Event("Test Gala", null, "Loc", "Cat");
                fakeEvent.setEventId("e1");
                fakeEvent.setStatus("active");
                adapter.submitList(Arrays.asList(fakeEvent));

                // Force layout measurement
                rv.measure(0, 0);
                rv.layout(0, 0, 1000, 1000);

                // Grab the view and click the manage button
                RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(0);
                assertNotNull(holder);
                holder.itemView.findViewById(R.id.btnCancelEvent).performClick();

                // Grab the opened dialog and click Suspend
                AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
                assertNotNull(dialog);
                dialog.findViewById(R.id.btnSuspendEvent).performClick();
            });
        }
    }

    @Test
    public void testMyReservationsCancelDialog() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MyReservationsActivity.class);
        intent.putExtra("USER_ID", "test-user-123");

        try (ActivityScenario<MyReservationsActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                RecyclerView rv = activity.findViewById(R.id.reservationsRecyclerView);
                ReservationAdapter adapter = (ReservationAdapter) rv.getAdapter();

                Reservation fakeRes = new Reservation("u1", "e1", "Test", null, "Loc", "Cat");
                fakeRes.setReservationId("res1");
                adapter.submitList(Arrays.asList(fakeRes));

                rv.measure(0, 0);
                rv.layout(0, 0, 1000, 1000);

                RecyclerView.ViewHolder holder = rv.findViewHolderForAdapterPosition(0);
                assertNotNull(holder);
                holder.itemView.findViewById(R.id.btnCancelReservation).performClick();

                AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
                assertNotNull(dialog);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
            });
        }
    }

    @Test
    public void testAddEditEventEditMode() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddEditEventActivity.class);
        intent.putExtra("EVENT_ID", "event-123");
        intent.putExtra("EVENT_TITLE", "Existing Event");
        intent.putExtra("EVENT_LOCATION", "Existing Loc");
        intent.putExtra("EVENT_CATEGORY", "Music");
        intent.putExtra("EVENT_STATUS", "active");
        intent.putExtra("EVENT_TICKETS_BOOKED", 10);
        intent.putExtra("EVENT_CAPACITY", 50);

        try (ActivityScenario<AddEditEventActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                TextView tvHeader = activity.findViewById(R.id.tvAdminTitle);
                assertEquals("Edit Event", tvHeader.getText().toString());

                // Click save to trigger the update database path
                activity.findViewById(R.id.btnSaveEvent).performClick();
            });
        }
    }

    @Test
    public void testDiffUtils() {
        EventAdapter adapter = new EventAdapter(false, null, null);
        Event e1 = new Event("A", null, "B", "C"); e1.setEventId("1");
        Event e2 = new Event("A", null, "B", "C"); e2.setEventId("1");
        adapter.submitList(Arrays.asList(e1));
        adapter.submitList(Arrays.asList(e2));
    }
}