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
        db.collection("events").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Event> events = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                events.add(doc.toObject(Event.class));
            }
            data.setValue(events);
        }).addOnFailureListener(Throwable::printStackTrace);
        return data;
    }
}