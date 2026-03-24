package com.example.ticketreservationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

public class EventAdapter extends ListAdapter<Event, EventAdapter.EventViewHolder> {

    public EventAdapter() {
        super(new DiffUtil.ItemCallback<Event>() {
            @Override
            public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                // Safely compare titles even if one is null
                return Objects.equals(oldItem.getTitle(), newItem.getTitle());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
                // Safely compare both fields
                return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                        Objects.equals(oldItem.getLocation(), newItem.getLocation());
            }
        });
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
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvLocation, tvCategory;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}