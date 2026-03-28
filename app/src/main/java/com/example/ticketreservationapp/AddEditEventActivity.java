package com.example.ticketreservationapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {

    private EditText etTitle, etLocation, etCategory;
    private Button btnSave, btnPickDate, btnPickTime;
    private TextView tvHeader;
    private EventViewModel eventViewModel;
    private String editEventId = null;

    private Calendar eventCalendar = Calendar.getInstance();
    private boolean isDateSelected = false;
    private boolean isTimeSelected = false;

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

        if (title.isEmpty() || location.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all text fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that Date and Time were picked
        if (!isDateSelected || !isTimeSelected) {
            Toast.makeText(this, "Please select both a Date and a Time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert our Calendar into a Firebase Timestamp
        Timestamp firebaseTimestamp = new Timestamp(eventCalendar.getTime());

        Event event = new Event(title, firebaseTimestamp, location, category);

        if (editEventId == null) {
            // Add
            eventViewModel.addEvent(event, success -> {
                if(success) finish();
            });
        } else {
            // Edit
            eventViewModel.updateEvent(editEventId, event, success -> {
                if(success) finish();
            });
        }
    }
}