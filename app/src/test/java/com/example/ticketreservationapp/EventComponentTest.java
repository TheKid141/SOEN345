package com.example.ticketreservationapp;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class EventComponentTest {

    /**
     * US 4.2: Component Test verifying database update logic
     * This tests the interaction between the EventViewModel and the EventRepository.
     */
    @Test
    void shouldCallRepositoryUpdateWhenEditingEvent() {
        EventRepository mockRepo = mock(EventRepository.class);
        EventViewModel viewModel = new EventViewModel(mockRepo);

        Event updatedEvent = new Event("Updated Concert", null, "Toronto", "Festival");
        EventRepository.ActionCallback mockCallback = mock(EventRepository.ActionCallback.class);

        viewModel.updateEvent("event123", updatedEvent, mockCallback);

        verify(mockRepo).updateEvent(eq("event123"), eq(updatedEvent), eq(mockCallback));
    }
}