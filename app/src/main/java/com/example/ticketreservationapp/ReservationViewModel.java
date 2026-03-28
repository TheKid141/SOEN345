package com.example.ticketreservationapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class ReservationViewModel extends ViewModel {
    private final ReservationRepository repository;

    public ReservationViewModel() {
        this.repository = new ReservationRepository();
    }


    public ReservationViewModel(ReservationRepository testRepository) {
        this.repository = testRepository;
    }

    public LiveData<List<Reservation>> getReservationsForUser(String userId) {
        return repository.getReservationsForUser(userId);
    }

    public void createReservation(Reservation reservation,
                                  ReservationRepository.ReservationCallback callback) {
        repository.createReservation(reservation, callback);
    }

    public void cancelReservation(String reservationId,
                                  ReservationRepository.ReservationCallback callback) {
        repository.cancelReservation(reservationId, callback);
    }

    public void hasActiveReservation(String userId, String eventId,
                                     ReservationRepository.ExistsCallback callback) {
        repository.hasActiveReservation(userId, eventId, callback);
    }

}
