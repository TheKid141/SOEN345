package com.example.ticketreservationapp;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;
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
        DocumentReference reservationRef = db.collection(COLLECTION).document();
        DocumentReference eventRef = db.collection("events").document(reservation.getEventId());
        reservation.setReservationId(reservationRef.getId());

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot eventSnapshot = transaction.get(eventRef);
            if (!eventSnapshot.exists()) {
                throw new IllegalStateException("Event not found");
            }

            Event event = eventSnapshot.toObject(Event.class);
            if (event == null) {
                throw new IllegalStateException("Event not found");
            }

            if ("cancelled".equals(event.getStatus())) {
                throw new IllegalStateException("Event is cancelled");
            }

            if (event.isSoldOut()) {
                throw new IllegalStateException("Event is sold out");
            }

            transaction.set(reservationRef, reservation);
            transaction.update(eventRef, "ticketsBooked", event.getTicketsBooked() + 1);
            return null;
        }).addOnSuccessListener(unused -> callback.onResult(true, reservationRef.getId()))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.onResult(false, null);
                });
    }

    public void cancelReservation(String reservationId, ReservationCallback callback) {
        DocumentReference reservationRef = db.collection(COLLECTION).document(reservationId);

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot reservationSnapshot = transaction.get(reservationRef);
            if (!reservationSnapshot.exists()) {
                throw new IllegalStateException("Reservation not found");
            }

            Reservation reservation = reservationSnapshot.toObject(Reservation.class);
            if (reservation == null) {
                throw new IllegalStateException("Reservation not found");
            }

            if ("cancelled".equals(reservation.getStatus())) {
                return null;
            }
    
            String eventId = reservation.getEventId();
            if (eventId != null && !eventId.trim().isEmpty()) {
                DocumentReference eventRef = db.collection("events").document(eventId);
                DocumentSnapshot eventSnapshot = transaction.get(eventRef);
                if (eventSnapshot.exists()) {
                    Event event = eventSnapshot.toObject(Event.class);
                    if (event != null) {
                        transaction.update(eventRef, "ticketsBooked", Math.max(0, event.getTicketsBooked() - 1));
                    }
                }
            }

            transaction.update(reservationRef, "status", "cancelled");
            return null;
        }).addOnSuccessListener(unused -> callback.onResult(true, reservationId))
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