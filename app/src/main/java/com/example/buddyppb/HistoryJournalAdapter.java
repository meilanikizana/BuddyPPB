package com.example.buddyppb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddyppb.data.JournalEntry;

import java.util.List;

public class HistoryJournalAdapter extends RecyclerView.Adapter<HistoryJournalAdapter.ViewHolder> {

    private final List<JournalEntry> history;

    public HistoryJournalAdapter(List<JournalEntry> history) {
        this.history = history;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.tv_item_date);
            titleTextView = itemView.findViewById(R.id.tv_item_title);
        }
    }

    @NonNull
    @Override
    public HistoryJournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journal_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryJournalAdapter.ViewHolder holder, int position) {
        JournalEntry item = history.get(position);

        holder.dateTextView.setText(item.getDate().toString());
        holder.titleTextView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return history.size();
    }
}

