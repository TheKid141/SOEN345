package com.example.ticketreservationapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class EventViewModel extends ViewModel {
    private EventRepository repository;
    private LiveData<List<Event>> eventsLiveData;

    // Standard constructor for the real app
    public EventViewModel() {
        this.repository = new EventRepository();
        this.eventsLiveData = repository.getEvents();
    }

    // Overloaded constructor for testing (so we can pass in a Mockito fake)
    public EventViewModel(EventRepository testRepository) {
        this.repository = testRepository;
        this.eventsLiveData = repository.getEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return eventsLiveData;
    }
}