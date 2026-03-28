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

    //For Admin Portal
    public void addEvent(Event event, EventRepository.ActionCallback callback) {
        repository.addEvent(event, callback);
    }

    public void updateEvent(String eventId, Event event, EventRepository.ActionCallback callback) {
        repository.updateEvent(eventId, event, callback);
    }

    public void cancelEvent(String eventId, EventRepository.ActionCallback callback) {
        repository.cancelEvent(eventId, callback);
    }

    public void restoreEvent(String eventId, EventRepository.ActionCallback callback) {
        repository.restoreEvent(eventId, callback);
    }

    public void deleteEvent(String eventId, EventRepository.ActionCallback callback) {
        repository.deleteEvent(eventId, callback);
    }
}