package com.example.ticketreservationapp;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<List<Event>> getEvents() {
        MutableLiveData<List<Event>> data = new MutableLiveData<>();
        db.collection("events").addSnapshotListener((snapshots, e) -> {
            if (e != null) return;
            if (snapshots != null) {
                List<Event> events = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    Event event = doc.toObject(Event.class);
                    event.setEventId(doc.getId());
                    // REMOVED THE FILTER: We now fetch ALL events from the database
                    events.add(event);
                }
                data.setValue(events);
            }
        });
        return data;
    }

    public void restoreEvent(String eventId, ActionCallback callback) {
        db.collection("events").document(eventId).update("status", "active")
                .addOnSuccessListener(aVoid -> callback.onResult(true))
                .addOnFailureListener(e -> callback.onResult(false));
    }

    public void deleteEvent(String eventId, ActionCallback callback) {
        db.collection("events").document(eventId).delete()
                .addOnSuccessListener(aVoid -> callback.onResult(true))
                .addOnFailureListener(e -> callback.onResult(false));
    }

    public void addEvent(Event event, ActionCallback callback) {
        db.collection("events").add(event)
                .addOnSuccessListener(docRef -> callback.onResult(true))
                .addOnFailureListener(e -> callback.onResult(false));
    }

    public void updateEvent(String eventId, Event updatedEvent, ActionCallback callback) {
        db.collection("events").document(eventId).set(updatedEvent)
                .addOnSuccessListener(aVoid -> callback.onResult(true))
                .addOnFailureListener(e -> callback.onResult(false));
    }

    public void cancelEvent(String eventId, ActionCallback callback) {
        db.collection("events").document(eventId).update("status", "cancelled")
                .addOnSuccessListener(aVoid -> callback.onResult(true))
                .addOnFailureListener(e -> callback.onResult(false));
    }

    public interface ActionCallback {
        void onResult(boolean success);
    }
}