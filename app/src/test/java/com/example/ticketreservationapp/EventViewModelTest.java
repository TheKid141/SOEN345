package com.example.ticketreservationapp;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;
import androidx.lifecycle.MutableLiveData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventViewModelTest {

    @Mock
    private EventRepository mockRepository;

    private EventViewModel viewModel;

    @BeforeEach
    void setUp() {
        ArchTaskExecutor.getInstance().setDelegate(new TaskExecutor() {
            @Override
            public void executeOnDiskIO(@NonNull Runnable runnable) { runnable.run(); }
            @Override
            public void postToMainThread(@NonNull Runnable runnable) { runnable.run(); }
            @Override
            public boolean isMainThread() { return true; }
        });

        MockitoAnnotations.openMocks(this);

        List<Event> fakeList = new ArrayList<>();
        fakeList.add(new Event("Fake Event", null, "Fake City", "Fake Cat"));

        MutableLiveData<List<Event>> fakeLiveData = new MutableLiveData<>();
        fakeLiveData.setValue(fakeList);

        when(mockRepository.getEvents()).thenReturn(fakeLiveData);

        viewModel = new EventViewModel(mockRepository);
    }

    @AfterEach
    void tearDown() {
        ArchTaskExecutor.getInstance().setDelegate(null);
    }

    @Test
    void testGetEvents() {
        List<Event> result = viewModel.getEvents().getValue();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fake Event", result.get(0).getTitle());

        verify(mockRepository, times(1)).getEvents();
    }

    @Test
    void testUpdateEvent() {
        Event updatedEvent = new Event("Updated", null, "Tor", "Art");
        EventRepository.ActionCallback mockCallback = mock(EventRepository.ActionCallback.class);

        viewModel.updateEvent("e123", updatedEvent, mockCallback);

        ArgumentCaptor<EventRepository.ActionCallback> captor = ArgumentCaptor.forClass(EventRepository.ActionCallback.class);
        verify(mockRepository).updateEvent(eq("e123"), eq(updatedEvent), captor.capture());

        captor.getValue().onResult(true);
        verify(mockCallback).onResult(true);
    }

    @Test
    void testAddEvent() {
        Event newEvent = new Event("New", null, "MTL", "Music");
        EventRepository.ActionCallback mockCallback = mock(EventRepository.ActionCallback.class);

        viewModel.addEvent(newEvent, mockCallback);

        ArgumentCaptor<EventRepository.ActionCallback> captor = ArgumentCaptor.forClass(EventRepository.ActionCallback.class);
        verify(mockRepository).addEvent(eq(newEvent), captor.capture());

        captor.getValue().onResult(true);
        verify(mockCallback).onResult(true);
    }

    @Test
    void testDeleteEvent() {
        EventRepository.ActionCallback mockCallback = mock(EventRepository.ActionCallback.class);
        viewModel.deleteEvent("e123", mockCallback);

        ArgumentCaptor<EventRepository.ActionCallback> captor = ArgumentCaptor.forClass(EventRepository.ActionCallback.class);
        verify(mockRepository).deleteEvent(eq("e123"), captor.capture());

        captor.getValue().onResult(true);
        verify(mockCallback).onResult(true);
    }

    @Test
    void testCancelEvent() {
        EventRepository.ActionCallback mockCallback = mock(EventRepository.ActionCallback.class);
        viewModel.cancelEvent("e123", mockCallback);

        ArgumentCaptor<EventRepository.ActionCallback> captor = ArgumentCaptor.forClass(EventRepository.ActionCallback.class);
        verify(mockRepository).cancelEvent(eq("e123"), captor.capture());

        captor.getValue().onResult(true);
        verify(mockCallback).onResult(true);
    }

    @Test
    void testRestoreEvent() {
        EventRepository.ActionCallback mockCallback = mock(EventRepository.ActionCallback.class);
        viewModel.restoreEvent("e123", mockCallback);

        ArgumentCaptor<EventRepository.ActionCallback> captor = ArgumentCaptor.forClass(EventRepository.ActionCallback.class);
        verify(mockRepository).restoreEvent(eq("e123"), captor.capture());

        captor.getValue().onResult(true);
        verify(mockCallback).onResult(true);
    }
}