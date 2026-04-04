package com.example.ticketreservationapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

public class EventAdapter extends ListAdapter<Event, EventAdapter.EventViewHolder> {

    private Set<String> reservedEventIds = new HashSet<>();
    private boolean isAdmin = false;

    public interface OnReserveClickListener {
        void onReserveClick(Event event);
    }

    public interface OnAdminActionClickListener {
        void onEditClick(Event event);
        void onCancelEventClick(Event event);
    }

    private OnReserveClickListener reserveListener;
    private OnAdminActionClickListener adminListener;

    public EventAdapter(boolean isAdmin, OnReserveClickListener reserveListener, OnAdminActionClickListener adminListener) {
        super(DIFF_CALLBACK);
        this.isAdmin = isAdmin;
        this.reserveListener = reserveListener;
        this.adminListener = adminListener;
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Event>() {
                @Override
                public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    return Objects.equals(oldItem.getEventId(), newItem.getEventId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                            Objects.equals(oldItem.getLocation(), newItem.getLocation()) &&
                            Objects.equals(oldItem.getCategory(), newItem.getCategory()) &&
                            Objects.equals(oldItem.getStatus(), newItem.getStatus()) &&
                            oldItem.getCapacity() == newItem.getCapacity() &&
                            oldItem.getTicketsBooked() == newItem.getTicketsBooked();
                }
            };


    public void setReservedEventIds(Set<String> ids) {
        reservedEventIds = ids;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = getItem(position);
        holder.tvTitle.setText(event.getTitle());

        String dateStr = event.getFormattedDate() != null ? event.getFormattedDate() : "TBD";
        String timeStr = event.getDateTime() != null ? event.getDateTime() : "TBD";
        android.content.Context context = holder.itemView.getContext();

        holder.tvDate.setText(context.getString(R.string.event_date_format, dateStr, timeStr));
        holder.tvLocation.setText(context.getString(R.string.event_location_format, event.getLocation()));
        holder.tvCategory.setText(context.getString(R.string.event_category_format, event.getCategory()));

        if (holder.tvCapacity != null) {
            if (event.getCapacity() > 0) {
                holder.tvCapacity.setVisibility(View.VISIBLE);
                int available = event.getAvailableTickets();
                holder.tvCapacity.setText(context.getString(
                        R.string.event_capacity_format, available, event.getCapacity()));
                if (event.isSoldOut()) {
                    holder.tvCapacity.setTextColor(Color.RED);
                } else if (available <= Math.max(1, event.getCapacity() / 10)) {
                    holder.tvCapacity.setTextColor(Color.parseColor("#E65100")); // orange warning
                } else {
                    holder.tvCapacity.setTextColor(Color.parseColor("#388E3C")); // green
                }
            } else {
                holder.tvCapacity.setVisibility(View.GONE);
            }
        }




        boolean isSuspended = "cancelled".equals(event.getStatus());
        boolean isSoldOut   = event.isSoldOut();

        if (isAdmin) {
            holder.btnReserve.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.VISIBLE);

            if (isSuspended) {
                holder.tvStatus.setText("Status: Cancelled");
                holder.tvStatus.setTextColor(Color.RED);
            } else {
                holder.tvStatus.setText("Status: Active");
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
            }

            if (holder.adminControlsLayout != null) {
                holder.adminControlsLayout.setVisibility(View.VISIBLE);
                holder.btnCancelEvent.setText(context.getString(R.string.btn_manage));
                holder.btnEditEvent.setOnClickListener(v -> {
                    if (adminListener != null) adminListener.onEditClick(event);
                });
                holder.btnCancelEvent.setOnClickListener(v -> {
                    if (adminListener != null) adminListener.onCancelEventClick(event);
                });
            }
        } else {
            // --- Customer View ---
            if (holder.adminControlsLayout != null) holder.adminControlsLayout.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.GONE);
            holder.btnReserve.setVisibility(View.VISIBLE);
            holder.btnReserve.setOnClickListener(null);

            if (isSuspended) {
                holder.btnReserve.setText("Cancelled");
                holder.btnReserve.setEnabled(false);
                holder.btnReserve.setAlpha(1.0f);
                holder.btnReserve.setTextColor(Color.WHITE);
                holder.btnReserve.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D32F2F"))); // Red Warning Color
            } else if(isSoldOut){
                holder.btnReserve.setText("Sold Out");
                holder.btnReserve.setEnabled(false);
                holder.btnReserve.setAlpha(1.0f);
                holder.btnReserve.setTextColor(Color.WHITE);
                holder.btnReserve.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#757575")));
            } else if (reservedEventIds.contains(event.getEventId())) {
                holder.btnReserve.setText("Reserved");
                holder.btnReserve.setEnabled(false);
                holder.btnReserve.setAlpha(1.0f);
                holder.btnReserve.setTextColor(Color.WHITE);
                holder.btnReserve.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#757575"))); // Dark Grey
            } else {
                holder.btnReserve.setText("Reserve Ticket");
                holder.btnReserve.setEnabled(true);
                holder.btnReserve.setAlpha(1.0f);
                holder.btnReserve.setTextColor(Color.WHITE);
                holder.btnReserve.setBackgroundTintList(null);
                holder.btnReserve.setOnClickListener(v -> reserveListener.onReserveClick(event));
            }
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvLocation, tvCategory, tvStatus, tvCapacity;
        Button btnReserve;
        LinearLayout adminControlsLayout;
        Button btnEditEvent, btnCancelEvent;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            btnReserve = itemView.findViewById(R.id.btnReserve);
            adminControlsLayout = itemView.findViewById(R.id.adminControlsLayout);
            btnEditEvent = itemView.findViewById(R.id.btnEditEvent);
            btnCancelEvent = itemView.findViewById(R.id.btnCancelEvent);
        }
    }
}