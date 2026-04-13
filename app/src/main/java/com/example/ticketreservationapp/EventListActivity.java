package com.example.ticketreservationapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private ReservationViewModel reservationViewModel;
    private Button btnFilter, btnLogout, btnMyReservations;
    private FloatingActionButton fabAddEvent;
    private Calendar selectedCalendar = null;
    private String selectedCategory = "All";
    private String selectedLocation = "All";
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        isAdmin = getIntent().getBooleanExtra("IS_ADMIN", false);

        recyclerView = findViewById(R.id.recyclerView);
        btnFilter = findViewById(R.id.btnFilter);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyReservations = findViewById(R.id.btnMyReservations);
        fabAddEvent = findViewById(R.id.fabAddEvent);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        if (isAdmin) {
            fabAddEvent.setVisibility(View.VISIBLE);
            btnMyReservations.setVisibility(View.GONE);
            fabAddEvent.setOnClickListener(v -> {
                Intent intent = new Intent(EventListActivity.this, AddEditEventActivity.class);
                startActivity(intent);
            });
        } else {
            fabAddEvent.setVisibility(View.GONE);
            btnMyReservations.setVisibility(View.VISIBLE);
        }

        adapter = new EventAdapter(isAdmin, this::handleReserveClick, new EventAdapter.OnAdminActionClickListener() {
            @Override
            public void onEditClick(Event event) {
                Intent intent = new Intent(EventListActivity.this, AddEditEventActivity.class);
                intent.putExtra("EVENT_ID", event.getEventId());
                intent.putExtra("EVENT_TITLE", event.getTitle());
                intent.putExtra("EVENT_LOCATION", event.getLocation());
                intent.putExtra("EVENT_CATEGORY", event.getCategory());
                intent.putExtra("EVENT_CAPACITY",  event.getCapacity());
                intent.putExtra("EVENT_STATUS", event.getStatus());
                intent.putExtra("EVENT_TICKETS_BOOKED", event.getTicketsBooked());
                intent.putExtra("EVENT_TIMESTAMP", event.getRawDate() != null ? event.getRawDate().getTime() : -1);
                startActivity(intent);
            }

            @Override
            public void onCancelEventClick(Event event) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventListActivity.this);
                View view = LayoutInflater.from(EventListActivity.this).inflate(R.layout.dialog_manage_event, null);
                builder.setView(view);
                AlertDialog dialog = builder.create();

                TextView tvTitle = view.findViewById(R.id.tvManageDialogTitle);
                Button btnSuspend = view.findViewById(R.id.btnSuspendEvent);
                Button btnRestore = view.findViewById(R.id.btnRestoreEvent);
                Button btnDelete = view.findViewById(R.id.btnDeleteEvent);

                tvTitle.setText(getString(R.string.title_manage_event, event.getTitle()));

                if ("cancelled".equals(event.getStatus())) {
                    btnSuspend.setVisibility(View.GONE);
                    btnRestore.setVisibility(View.VISIBLE);
                } else {
                    btnSuspend.setVisibility(View.VISIBLE);
                    btnRestore.setVisibility(View.GONE);
                }

                btnSuspend.setOnClickListener(v -> {
                    eventViewModel.cancelEvent(event.getEventId(), success -> {
                        if(success) Snackbar.make(findViewById(android.R.id.content), getString(R.string.snackbar_suspended), Snackbar.LENGTH_SHORT).show();
                    });
                    dialog.dismiss();
                });

                btnRestore.setOnClickListener(v -> {
                    eventViewModel.restoreEvent(event.getEventId(), success -> {
                        if(success) Snackbar.make(findViewById(android.R.id.content), getString(R.string.snackbar_restored), Snackbar.LENGTH_SHORT).show();
                    });
                    dialog.dismiss();
                });

                btnDelete.setOnClickListener(v -> {
                    dialog.dismiss();
                    new AlertDialog.Builder(EventListActivity.this)
                            .setTitle(getString(R.string.title_permanent_delete))
                            .setMessage(getString(R.string.msg_permanent_delete))
                            .setPositiveButton("Delete", (d, w) -> {
                                eventViewModel.deleteEvent(event.getEventId(), success -> {
                                    if(success) Snackbar.make(findViewById(android.R.id.content), getString(R.string.snackbar_deleted), Snackbar.LENGTH_SHORT).show();
                                });
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                });
                dialog.show();
            }
        });

        recyclerView.setAdapter(adapter);

        // Initial Data Load
        loadEvents();
        if (!isAdmin) {
            loadReservedEventIds();
        }

        btnFilter.setOnClickListener(v -> showFilterDialog());

        btnMyReservations.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(EventListActivity.this, MyReservationsActivity.class);
                intent.putExtra("USER_ID", currentUser.getUid());
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: Not logged in.", Snackbar.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(EventListActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadEvents() {
        eventViewModel.getEvents().observe(this, events -> {
            fullEventList = events;
            applyFilters();
        });
    }

    private void loadReservedEventIds() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        reservationViewModel.getReservationsForUser(currentUser.getUid()).observe(this, reservations -> {
            if (reservations == null) return;

            Set<String> ids = new HashSet<>();
            for (Reservation r : reservations) {
                if (r.getEventId() != null) {
                    ids.add(r.getEventId());
                }
            }

            // This forces the adapter to wait until the UI is ready before updating buttons
            recyclerView.post(() -> adapter.setReservedEventIds(ids));
        });
    }

    private void handleReserveClick(Event event) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String eventId = event.getEventId();

        reservationViewModel.hasActiveReservation(currentUser.getUid(), eventId, exists -> {
            if (exists) {
                Snackbar.make(findViewById(android.R.id.content),
                        "You already have a reservation for this event.",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                confirmReservation(event, currentUser.getUid(), eventId, currentUser);
            }
        });
    }

    private void confirmReservation(Event event, String userId, String eventId, FirebaseUser currentUser) {
        new AlertDialog.Builder(this)
                .setTitle("Reserve Ticket")
                .setMessage("Reserve a ticket for \"" + event.getTitle() + "\"?")
                .setPositiveButton("Reserve", (dialog, which) -> {
                    Reservation reservation = new Reservation(userId, eventId, event.getTitle(), event.getDate(), event.getLocation(), event.getCategory());
                    reservationViewModel.createReservation(reservation, (success, id) -> {
                        if (success) {
                            sendConfirmationEmail(
                                    currentUser.getEmail(),
                                    event.getTitle(),
                                    event.getFormattedDate() + " @ " + event.getDateTime(),
                                    event.getLocation(),
                                    id
                            );
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Ticket reserved! View in My Reservations.",
                                    Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Reservation failed. Please try again.",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
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

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryList);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(catAdapter);
        categorySpinner.setSelection(categoryList.indexOf(selectedCategory));

        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, locationList);
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locAdapter);
        locationSpinner.setSelection(locationList.indexOf(selectedLocation));
    }

    private void applyFilters() {
        adapter.submitList(new EventFilterHelper().filterEvents(fullEventList, selectedCalendar, selectedCategory, selectedLocation));
    }

    private void sendConfirmationEmail(String userEmail, String eventTitle, String eventDate, String eventLocation, String reservationId) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.emailjs.com/api/v1.0/email/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonBody = "{"
                        + "\"service_id\":\"service_5dm9jx8\","
                        + "\"template_id\":\"template_g0ivr5n\","
                        + "\"user_id\":\"bVc2hfTXfgAcHw3dr\","
                        + "\"template_params\":{"
                        + "\"user_email\":\"" + userEmail + "\","
                        + "\"event_title\":\"" + eventTitle + "\","
                        + "\"event_date\":\"" + eventDate + "\","
                        + "\"event_location\":\"" + eventLocation + "\","
                        + "\"reservation_id\":\"" + reservationId + "\""
                        + "}"
                        + "}";

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    runOnUiThread(() ->
                            Snackbar.make(findViewById(android.R.id.content),
                                    "Confirmation email sent!", Snackbar.LENGTH_SHORT).show()
                    );
                } else {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) errorResponse.append(line);
                    br.close();
                    Log.e("EmailJS", "Error " + responseCode + ": " + errorResponse);

                    runOnUiThread(() -> Snackbar.make(findViewById(android.R.id.content),
                            "Reservation confirmed but email failed to send.",
                            Snackbar.LENGTH_SHORT).show());
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}