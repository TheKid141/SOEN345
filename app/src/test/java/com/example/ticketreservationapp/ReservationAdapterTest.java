package com.example.ticketreservationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ReservationAdapterTest {

    private ReservationAdapter adapter;
    private ReservationAdapter.OnCancelClickListener mockListener;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        mockListener = mock(ReservationAdapter.OnCancelClickListener.class);
        adapter = new ReservationAdapter(mockListener);
    }

    @Test
    public void testAdapterItemCount() {
        List<Reservation> reservations = Arrays.asList(
                new Reservation("u1", "e1", "Event 1", null, "Loc 1", "Cat 1"),
                new Reservation("u1", "e2", "Event 2", null, "Loc 2", "Cat 2")
        );
        adapter.submitList(reservations);
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testViewHolderBinding() {
        Reservation res = new Reservation("u1", "e1", "Test Gala", null, "Montreal", "Social");
        adapter.submitList(Arrays.asList(res));

        // Create a parent for the view holder
        FrameLayout parent = new FrameLayout(context);
        ReservationAdapter.ReservationViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        adapter.onBindViewHolder(holder, 0);

        assertEquals("Test Gala", holder.tvTitle.getText().toString());
        assertEquals("📍 Montreal", holder.tvLocation.getText().toString());
        assertEquals("🏷️ Social", holder.tvCategory.getText().toString());
    }

    @Test
    public void testCancelReservationButtonTriggersListener() {
        Reservation res = new Reservation("u1", "e1", "Test Gala", null, "Montreal", "Social");
        adapter.submitList(Arrays.asList(res));

        FrameLayout parent = new FrameLayout(context);
        ReservationAdapter.ReservationViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        // Simulate user clicking "Cancel" on their ticket
        holder.btnCancel.performClick();
        verify(mockListener).onCancelClick(res);
    }
}
