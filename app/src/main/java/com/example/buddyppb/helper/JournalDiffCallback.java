package com.example.buddyppb.helper;

import androidx.recyclerview.widget.DiffUtil;
import com.example.buddyppb.data.Journal;

import java.util.List;

public class JournalDiffCallback extends DiffUtil.Callback {
    private final List<Journal> oldJournalList;
    private final List<Journal> newJournalList;

    public JournalDiffCallback(List<Journal> oldJournalList, List<Journal> newJournalList) {
        this.oldJournalList = oldJournalList;
        this.newJournalList = newJournalList;
    }

    @Override
    public int getOldListSize() {
        return oldJournalList.size();
    }

    @Override
    public int getNewListSize() {
        return newJournalList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldJournalList.get(oldItemPosition).getId() == newJournalList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Journal oldHistory = oldJournalList.get(oldItemPosition);
        Journal newHistory = newJournalList.get(newItemPosition);
        return oldHistory.getImage().equals(newHistory.getImage())
                && oldHistory.getTitle().equals(newHistory.getTitle())
                && oldHistory.getDescription().equals(newHistory.getDescription())
                && oldHistory.getTimestamp() == newHistory.getTimestamp();
    }
}