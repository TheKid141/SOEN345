package com.example.ticketreservationapp;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class EventAdapterTest {

    private Context context;
    private EventAdapter.OnReserveClickListener mockReserveListener;
    private EventAdapter.OnAdminActionClickListener mockAdminListener;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        mockReserveListener = mock(EventAdapter.OnReserveClickListener.class);
        mockAdminListener = mock(EventAdapter.OnAdminActionClickListener.class);
    }

    @Test
    public void testCustomerView() {
        EventAdapter adapter = new EventAdapter(false, mockReserveListener, mockAdminListener);
        Event event = new Event("Test", null, "Loc", "Cat");
        adapter.submitList(Arrays.asList(event));

        FrameLayout parent = new FrameLayout(context);
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        assertEquals(View.VISIBLE, holder.btnReserve.getVisibility());
        assertEquals(View.GONE, holder.adminControlsLayout.getVisibility());
        assertEquals(View.GONE, holder.tvStatus.getVisibility());
    }

    @Test
    public void testAdminView() {
        EventAdapter adapter = new EventAdapter(true, mockReserveListener, mockAdminListener);
        Event event = new Event("Test", null, "Loc", "Cat");
        adapter.submitList(Arrays.asList(event));

        FrameLayout parent = new FrameLayout(context);
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        assertEquals(View.GONE, holder.btnReserve.getVisibility());
        assertEquals(View.VISIBLE, holder.adminControlsLayout.getVisibility());
        assertEquals(View.VISIBLE, holder.tvStatus.getVisibility());
    }

    @Test
    public void testCustomerReserveButton() {
        EventAdapter adapter = new EventAdapter(false, mockReserveListener, mockAdminListener);
        Event event = new Event("Test Event", null, "Loc", "Cat");
        adapter.submitList(Arrays.asList(event));

        FrameLayout parent = new FrameLayout(context);
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        // Simulate a user clicking the button
        holder.btnReserve.performClick();

        // Verify the listener interface was called with the correct event
        verify(mockReserveListener).onReserveClick(event);
    }

    @Test
    public void testAdminEditButton() {
        EventAdapter adapter = new EventAdapter(true, mockReserveListener, mockAdminListener);
        Event event = new Event("Test Event", null, "Loc", "Cat");
        adapter.submitList(Arrays.asList(event));

        FrameLayout parent = new FrameLayout(context);
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        holder.btnEditEvent.performClick();
        verify(mockAdminListener).onEditClick(event);
    }

    @Test
    public void testAdminCancelButton() {
        EventAdapter adapter = new EventAdapter(true, mockReserveListener, mockAdminListener);
        Event event = new Event("Test Event", null, "Loc", "Cat");
        adapter.submitList(Arrays.asList(event));

        FrameLayout parent = new FrameLayout(context);
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        // Click the Suspend/Manage button
        holder.btnCancelEvent.performClick();
        verify(mockAdminListener).onCancelEventClick(event);
    }
}