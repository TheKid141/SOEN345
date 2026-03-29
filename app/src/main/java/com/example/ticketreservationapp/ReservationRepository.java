package com.example.ticketreservationapp;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION = "reservations";

    public MutableLiveData<List<Reservation>> getReservationsForUser(String userId) {
        MutableLiveData<List<Reservation>> data = new MutableLiveData<>();
        db.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "active")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }
                    if (snapshots != null) {
                        List<Reservation> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Reservation r = doc.toObject(Reservation.class);
                            r.setReservationId(doc.getId());
                            list.add(r);
                        }
                        data.setValue(list);
                    }
                });
        return data;
    }

    public void createReservation(Reservation reservation, ReservationCallback callback) {
        DocumentReference ref = db.collection(COLLECTION).document();
        reservation.setReservationId(ref.getId());
        ref.set(reservation)
                .addOnSuccessListener(unused -> callback.onResult(true, ref.getId()))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.onResult(false, null);
                });
    }

    public void cancelReservation(String reservationId, ReservationCallback callback) {
        db.collection(COLLECTION).document(reservationId)
                .update("status", "cancelled")
                .addOnSuccessListener(unused -> callback.onResult(true, reservationId))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.onResult(false, null);
                });
    }

    public void hasActiveReservation(String userId, String eventId, ExistsCallback callback) {
        db.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("eventId", eventId)
                .whereEqualTo("status", "active")
                .get()
                .addOnSuccessListener(snapshots -> callback.onResult(!snapshots.isEmpty()))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.onResult(false);
                });
    }

    public interface ReservationCallback {
        void onResult(boolean success, String reservationId);
    }

    public interface ExistsCallback {
        void onResult(boolean exists);
    }
}