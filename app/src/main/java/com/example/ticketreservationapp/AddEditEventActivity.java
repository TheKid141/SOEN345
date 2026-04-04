package com.example.ticketreservationapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {

    private EditText etTitle, etLocation, etCategory, etCapacity;
    private Button btnSave, btnPickDate, btnPickTime;
    private TextView tvHeader;
    private EventViewModel eventViewModel;
    private String editEventId = null;
    private String existingStatus = "active";
    private int existingTicketsBooked = 0;

    private Calendar eventCalendar = Calendar.getInstance();
    private boolean isDateSelected = false;
    private boolean isTimeSelected = false;
    private InputValidator validator = new InputValidator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        etTitle = findViewById(R.id.etEventTitle);
        etLocation = findViewById(R.id.etEventLocation);
        etCategory = findViewById(R.id.etEventCategory);
        btnPickDate = findViewById(R.id.btnPickEventDate);
        btnPickTime = findViewById(R.id.btnPickEventTime);
        btnSave = findViewById(R.id.btnSaveEvent);
        tvHeader = findViewById(R.id.tvAdminTitle);
        etCapacity = findViewById(R.id.etEventCapacity);

        android.widget.ImageButton btnBack = findViewById(R.id.btnBackFromAddEdit);
        btnBack.setOnClickListener(v -> finish());

        // Date Picker Dialog
        btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                eventCalendar.set(Calendar.YEAR, year);
                eventCalendar.set(Calendar.MONTH, month);
                eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                isDateSelected = true;

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                btnPickDate.setText(sdf.format(eventCalendar.getTime()));
            }, eventCalendar.get(Calendar.YEAR), eventCalendar.get(Calendar.MONTH), eventCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time Picker Dialog
        btnPickTime.setOnClickListener(v -> {
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                eventCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                eventCalendar.set(Calendar.MINUTE, minute);
                isTimeSelected = true;

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                btnPickTime.setText(sdf.format(eventCalendar.getTime()));
            }, eventCalendar.get(Calendar.HOUR_OF_DAY), eventCalendar.get(Calendar.MINUTE), false).show();
        });

        // Check if we are in Edit Mode
        if (getIntent().hasExtra("EVENT_ID")) {
            editEventId = getIntent().getStringExtra("EVENT_ID");
            tvHeader.setText("Edit Event");
            etTitle.setText(getIntent().getStringExtra("EVENT_TITLE"));
            etLocation.setText(getIntent().getStringExtra("EVENT_LOCATION"));
            etCategory.setText(getIntent().getStringExtra("EVENT_CATEGORY"));
            existingStatus = getIntent().getStringExtra("EVENT_STATUS");
            if (existingStatus == null) existingStatus = "active";
            existingTicketsBooked = getIntent().getIntExtra("EVENT_TICKETS_BOOKED", 0);

            int existingCapacity = getIntent().getIntExtra("EVENT_CAPACITY", 0);
            if (existingCapacity > 0) etCapacity.setText(String.valueOf(existingCapacity));

            long timestamp = getIntent().getLongExtra("EVENT_TIMESTAMP", -1);
            if (timestamp != -1) {
                eventCalendar.setTimeInMillis(timestamp);
                isDateSelected = true;
                isTimeSelected = true;

                SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                btnPickDate.setText(sdfDate.format(eventCalendar.getTime()));

                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                btnPickTime.setText(sdfTime.format(eventCalendar.getTime()));
            }
        }

        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void saveEvent() {
        String title = etTitle.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String capStr = etCapacity.getText().toString().trim();

        // Validate the basic event details
        if (!validator.isEventInputValid(title, location, category, isDateSelected, isTimeSelected)) {
            Snackbar.make(findViewById(android.R.id.content), "Please fill all fields and select Date & Time", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String capacityError = validator.validateCapacity(capStr, existingTicketsBooked);
        if (capacityError != null) {
            etCapacity.setError(capacityError);
            return;
        }

        int capacity = capStr.isEmpty() ? 0 : Integer.parseInt(capStr);

        // Convert our Calendar into a Firebase Timestamp
        Timestamp firebaseTimestamp = new Timestamp(eventCalendar.getTime());

        Event event = new Event(title, firebaseTimestamp, location, category);
        event.setCapacity(capacity);
        event.setStatus(existingStatus);
        event.setTicketsBooked(existingTicketsBooked);

        if (editEventId == null) {
            // Add new even
            eventViewModel.addEvent(event, success -> {
                if(success) finish();
            });
        } else {
            eventViewModel.updateEvent(editEventId, event, success -> {
                if(success) finish();
            });
        }
    }
}