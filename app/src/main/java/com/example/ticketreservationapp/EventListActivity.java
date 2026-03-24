package com.example.ticketreservationapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth; // Added for logout
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> fullEventList = new ArrayList<>();
    private EventViewModel eventViewModel;
    private Button btnFilter, btnLogout; // Added logout button

    private Calendar selectedCalendar = null;
    private String selectedCategory = "All";
    private String selectedLocation = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = findViewById(R.id.recyclerView);
        btnFilter = findViewById(R.id.btnFilter);
        btnLogout = findViewById(R.id.btnLogout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEvents().observe(this, events -> {
            fullEventList = events;
            applyFilters();
        });

        btnFilter.setOnClickListener(v -> showFilterDialog());

        // Handle Logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(EventListActivity.this, LoginActivity.class);
            // Clear the back stack so they can't press back to get into the app
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        Button btnPickDate = dialogView.findViewById(R.id.btnPickDate);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Spinner spinnerLocation = dialogView.findViewById(R.id.spinnerLocation);
        Button btnClearFilters = dialogView.findViewById(R.id.btnClearFilters);
        Button btnApplyFilter = dialogView.findViewById(R.id.btnApplyFilter);

        if (selectedCalendar != null) {
            btnPickDate.setText(String.format("%02d/%02d/%d",
                    selectedCalendar.get(Calendar.DAY_OF_MONTH),
                    selectedCalendar.get(Calendar.MONTH) + 1,
                    selectedCalendar.get(Calendar.YEAR)));
        }

        setupSpinners(spinnerCategory, spinnerLocation);

        AlertDialog dialog = builder.create();

        btnPickDate.setOnClickListener(v -> {
            Calendar c = selectedCalendar != null ? selectedCalendar : Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                btnPickDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnApplyFilter.setOnClickListener(v -> {
            selectedCategory = spinnerCategory.getSelectedItem().toString();
            selectedLocation = spinnerLocation.getSelectedItem().toString();
            applyFilters();
            dialog.dismiss();
        });

        btnClearFilters.setOnClickListener(v -> {
            selectedCalendar = null;
            selectedCategory = "All";
            selectedLocation = "All";
            btnPickDate.setText("Select Date");
            spinnerCategory.setSelection(0);
            spinnerLocation.setSelection(0);
        });

        dialog.show();
    }

    private void setupSpinners(Spinner categorySpinner, Spinner locationSpinner) {
        Set<String> categories = new HashSet<>();
        Set<String> locations = new HashSet<>();
        categories.add("All");
        locations.add("All");

        for (Event event : fullEventList) {
            if (event.getCategory() != null) categories.add(event.getCategory());
            if (event.getLocation() != null) locations.add(event.getLocation());
        }

        List<String> categoryList = new ArrayList<>(categories);
        List<String> locationList = new ArrayList<>(locations);
        Collections.sort(categoryList);
        Collections.sort(locationList);

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(catAdapter);
        categorySpinner.setSelection(categoryList.indexOf(selectedCategory));

        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationList);
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locAdapter);
        locationSpinner.setSelection(locationList.indexOf(selectedLocation));
    }

    private void applyFilters() {
        List<Event> filteredList = new ArrayList<>();
        for (Event event : fullEventList) {
            boolean matchesDate = true;
            boolean matchesCategory = true;
            boolean matchesLocation = true;

            if (selectedCalendar != null && event.getRawDate() != null) {
                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(event.getRawDate());
                matchesDate = (eventCal.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                        eventCal.get(Calendar.DAY_OF_YEAR) == selectedCalendar.get(Calendar.DAY_OF_YEAR));
            }

            if (!selectedCategory.equals("All")) {
                matchesCategory = selectedCategory.equalsIgnoreCase(event.getCategory());
            }

            if (!selectedLocation.equals("All")) {
                matchesLocation = selectedLocation.equalsIgnoreCase(event.getLocation());
            }

            if (matchesDate && matchesCategory && matchesLocation) {
                filteredList.add(event);
            }
        }
        adapter.submitList(filteredList);
    }
}