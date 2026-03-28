package com.example.ticketreservationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;
import com.google.firebase.auth.FirebaseUser;

public class MyReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private ReservationViewModel reservationViewModel;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        recyclerView = findViewById(R.id.reservationsRecyclerView);
        tvEmpty = findViewById(R.id.tvNoReservations);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservationAdapter(this::confirmCancel);
        recyclerView.setAdapter(adapter);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            finish(); // Should not happen — guarded by login flow
            return;
        }

        loadReservations(currentUser.getUid());

        findViewById(R.id.btnBackFromReservations).setOnClickListener(v -> finish());
    }

    private void loadReservations(String userId) {
        reservationViewModel.getReservationsForUser(userId).observe(this, reservations -> {
            if (reservations == null || reservations.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.submitList(reservations);
            }
        });
    }

    private void confirmCancel(Reservation reservation) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Reservation")
                .setMessage("Are you sure you want to cancel your reservation for \"" + reservation.getEventTitle() + "\"?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> cancelReservation(reservation))
                .setNegativeButton("Keep it", null)
                .show();
    }

    private void sendCancellationEmail(Reservation reservation) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        new Thread(() -> {
            try {
                URL url = new URL("https://api.emailjs.com/api/v1.0/email/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonBody = "{"
                        + "\"service_id\":\"service_5dm9jx8\","
                        + "\"template_id\":\"template_7f7nftm\","
                        + "\"user_id\":\"bVc2hfTXfgAcHw3dr\","
                        + "\"template_params\":{"
                        + "\"user_email\":\"" + currentUser.getEmail() + "\","
                        + "\"event_title\":\"" + reservation.getEventTitle() + "\","
                        + "\"event_date\":\"" + reservation.getDate() + " @ " + reservation.getDateTime() + "\","
                        + "\"event_location\":\"" + reservation.getEventLocation() + "\","
                        + "\"reservation_id\":\"" + reservation.getReservationId() + "\""
                        + "}"
                        + "}";

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) errorResponse.append(line);
                    br.close();
                    Log.e("EmailJS", "Error " + responseCode + ": " + errorResponse);
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void cancelReservation(Reservation reservation) {
        reservationViewModel.cancelReservation(reservation.getReservationId(), (success, id) -> {
            if (success) {
                sendCancellationEmail(reservation);
                Snackbar.make(findViewById(android.R.id.content),
                        "Reservation cancelled.", Snackbar.LENGTH_SHORT).show();
                // Reload the list
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) loadReservations(user.getUid());
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Failed to cancel. Please try again.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}