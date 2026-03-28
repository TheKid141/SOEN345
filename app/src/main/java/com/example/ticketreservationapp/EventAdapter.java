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
import java.util.HashSet;
import java.util.Set;

public class EventAdapter extends ListAdapter<Event, EventAdapter.EventViewHolder> {

    private Set<String> reservedEventIds = new HashSet<>();

    public interface OnReserveClickListener {
        void onReserveClick(Event event);
    }

    private OnReserveClickListener reserveListener;

    public EventAdapter() {
        super(DIFF_CALLBACK);
    }

    public EventAdapter(OnReserveClickListener reserveListener) {
        super(DIFF_CALLBACK);
        this.reserveListener = reserveListener;
    }

    private static final DiffUtil.ItemCallback<Event> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Event>() {
                @Override
                public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    return Objects.equals(oldItem.getTitle(), newItem.getTitle());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                    return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                            Objects.equals(oldItem.getLocation(), newItem.getLocation());
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
        holder.tvDate.setText(holder.itemView.getContext().getString(R.string.event_date_format, event.getDate(), event.getDateTime()));
        holder.tvLocation.setText(holder.itemView.getContext().getString(R.string.event_location_format, event.getLocation()));
        holder.tvCategory.setText(holder.itemView.getContext().getString(R.string.event_category_format, event.getCategory()));

        if (reserveListener != null) {
            holder.btnReserve.setVisibility(View.VISIBLE);
            if (reservedEventIds.contains(event.getEventId())) {
                holder.btnReserve.setText("Reserved");
                holder.btnReserve.setEnabled(false);
                holder.btnReserve.setAlpha(0.5f);
            } else {
                holder.btnReserve.setText("Reserve Ticket");
                holder.btnReserve.setEnabled(true);
                holder.btnReserve.setAlpha(1.0f);
                holder.btnReserve.setOnClickListener(v -> reserveListener.onReserveClick(event));
            }
        } else {
            holder.btnReserve.setVisibility(View.GONE);
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvLocation, tvCategory;
        Button btnReserve; // ✅ Added

        public EventViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnReserve = itemView.findViewById(R.id.btnReserve); // ✅ Added
        }
    }
}