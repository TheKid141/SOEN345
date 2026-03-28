package com.example.ticketreservationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

public class ReservationAdapter extends ListAdapter<Reservation, ReservationAdapter.ReservationViewHolder> {

    public interface OnCancelClickListener {
        void onCancelClick(Reservation reservation);
    }

    private final OnCancelClickListener cancelListener;

    public ReservationAdapter(OnCancelClickListener cancelListener) {
        super(new DiffUtil.ItemCallback<Reservation>() {
            @Override
            public boolean areItemsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                return Objects.equals(oldItem.getReservationId(), newItem.getReservationId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                return Objects.equals(oldItem.getReservationId(), newItem.getReservationId()) &&
                        Objects.equals(oldItem.getStatus(), newItem.getStatus());
            }
        });
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = getItem(position);
        holder.tvTitle.setText(reservation.getEventTitle());
        holder.tvDate.setText("📅 " + reservation.getDate() + " @ " + reservation.getDateTime());
        holder.tvLocation.setText("📍 " + reservation.getEventLocation());
        holder.tvCategory.setText("🏷️ " + reservation.getEventCategory());
        holder.btnCancel.setOnClickListener(v -> cancelListener.onCancelClick(reservation));
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvLocation, tvCategory;
        Button btnCancel;

        public ReservationViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvReservationTitle);
            tvDate = itemView.findViewById(R.id.tvReservationDate);
            tvLocation = itemView.findViewById(R.id.tvReservationLocation);
            tvCategory = itemView.findViewById(R.id.tvReservationCategory);
            btnCancel = itemView.findViewById(R.id.btnCancelReservation);
        }
    }
}