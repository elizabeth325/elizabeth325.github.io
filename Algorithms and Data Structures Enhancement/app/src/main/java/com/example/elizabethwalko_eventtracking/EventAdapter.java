// EventAdapter.java
package com.example.elizabethwalko_eventtracking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<Event> eventList;
    private final OnDeleteClickListener onDeleteClickListener;

    public EventAdapter(List<Event> eventList, OnDeleteClickListener onDeleteClickListener) {
        this.eventList = eventList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventName.setText(event.getName());
        holder.eventDate.setText(event.getDate());
        holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(event.getId()));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventDate;
        Button deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
            deleteButton = itemView.findViewById(R.id.delete_event_button);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int eventId);
    }
}