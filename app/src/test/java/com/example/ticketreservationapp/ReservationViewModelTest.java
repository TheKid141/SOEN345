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

public class ReservationViewModelTest {

    @Mock
    private ReservationRepository mockRepository;

    private ReservationViewModel viewModel;

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
        viewModel = new ReservationViewModel(mockRepository);
    }

    @AfterEach
    void tearDown() {
        ArchTaskExecutor.getInstance().setDelegate(null);
    }

    @Test
    void testGetReservationsForUser_Success() {
        List<Reservation> mockList = new ArrayList<>();
        mockList.add(new Reservation("u1", "e1", "Event 1", null, "Loc 1", "Cat 1"));
        
        MutableLiveData<List<Reservation>> liveData = new MutableLiveData<>();
        liveData.setValue(mockList);

        when(mockRepository.getReservationsForUser("u1")).thenReturn(liveData);

        List<Reservation> result = viewModel.getReservationsForUser("u1").getValue();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Event 1", result.get(0).getEventTitle());
    }

    @Test
    void testCancelReservation_Success() {
        String resId = "res123";
        ReservationRepository.ReservationCallback mockCallback = mock(ReservationRepository.ReservationCallback.class);

        // Capture the callback passed to the repository
        viewModel.cancelReservation(resId, mockCallback);
        
        ArgumentCaptor<ReservationRepository.ReservationCallback> captor = 
                ArgumentCaptor.forClass(ReservationRepository.ReservationCallback.class);
        verify(mockRepository).cancelReservation(eq(resId), captor.capture());

        // Simulate success
        captor.getValue().onResult(true, resId);
        verify(mockCallback).onResult(true, resId);
    }

    @Test
    void testCancelReservation_Failure() {
        String resId = "res123";
        ReservationRepository.ReservationCallback mockCallback = mock(ReservationRepository.ReservationCallback.class);

        viewModel.cancelReservation(resId, mockCallback);
        
        ArgumentCaptor<ReservationRepository.ReservationCallback> captor = 
                ArgumentCaptor.forClass(ReservationRepository.ReservationCallback.class);
        verify(mockRepository).cancelReservation(eq(resId), captor.capture());

        // Simulate failure
        captor.getValue().onResult(false, null);
        verify(mockCallback).onResult(false, null);
    }

    @Test
    void testHasActiveReservation_True() {
        ReservationRepository.ExistsCallback mockCallback = mock(ReservationRepository.ExistsCallback.class);
        viewModel.hasActiveReservation("u1", "e1", mockCallback);

        ArgumentCaptor<ReservationRepository.ExistsCallback> captor = 
                ArgumentCaptor.forClass(ReservationRepository.ExistsCallback.class);
        verify(mockRepository).hasActiveReservation(eq("u1"), eq("e1"), captor.capture());

        captor.getValue().onResult(true);
        verify(mockCallback).onResult(true);
    }

    @Test
    void testGetReservationsForNullUserReturnsEmpty() {
        MutableLiveData<List<Reservation>> emptyData = new MutableLiveData<>(new ArrayList<>());
        when(mockRepository.getReservationsForUser(null)).thenReturn(emptyData);

        List<Reservation> result = viewModel.getReservationsForUser(null).getValue();
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateReservation_Success() {
        Reservation newRes = new Reservation("u1", "e1", "Gala", null, "MTL", "Social");
        ReservationRepository.ReservationCallback mockCallback = mock(ReservationRepository.ReservationCallback.class);

        viewModel.createReservation(newRes, mockCallback);

        ArgumentCaptor<ReservationRepository.ReservationCallback> captor =
                ArgumentCaptor.forClass(ReservationRepository.ReservationCallback.class);
        verify(mockRepository).createReservation(eq(newRes), captor.capture());

        captor.getValue().onResult(true, "res123");
        verify(mockCallback).onResult(true, "res123");
    }
}
