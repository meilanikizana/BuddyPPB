package com.example.buddyppb;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buddyppb.R;
import com.example.buddyppb.data.Journal;
import com.example.buddyppb.databinding.ItemJournalBinding;
import com.example.buddyppb.helper.JournalDiffCallback;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    /* Membandingkan list lama dengan list baru (Menggantikan NotifyDataSetChanged) */
    private final ArrayList<Journal> listJournal = new ArrayList<>();

    public void setListJournal(List<Journal> listJournal) {
        JournalDiffCallback diffCallback = new JournalDiffCallback(this.listJournal, listJournal);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.listJournal.clear();
        this.listJournal.addAll(listJournal);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemJournalBinding binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new JournalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        holder.bind(listJournal.get(position));
    }

    @Override
    public int getItemCount() {
        return listJournal.size();
    }

    public static class JournalViewHolder extends RecyclerView.ViewHolder {
        private final ItemJournalBinding binding;

        public JournalViewHolder(ItemJournalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Journal journal) {
            binding.tvItemTitle.setText(journal.getTitle());
            Uri imageUri = Uri.parse(journal.getImage());
            Glide.with(binding.imgItemImage.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.default_image_buddy)
                    .into(binding.imgItemImage);
            binding.tvItemDesc.setText(journal.getDescription());
            binding.tvTimestamp.setText(journal.getTimestamp());
        }
    }
}