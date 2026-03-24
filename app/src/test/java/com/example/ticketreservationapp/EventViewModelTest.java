package com.example.ticketreservationapp;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.TaskExecutor;
import androidx.lifecycle.MutableLiveData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testGetEventsReturnsDataFromRepository() {
        List<Event> result = viewModel.getEvents().getValue();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fake Event", result.get(0).getTitle());

        verify(mockRepository, times(1)).getEvents();
    }
}