package com.naman.accounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Model.Item;
import com.naman.accounts.R;

public class ItemAdapter extends ListAdapter<Item, ListViewHolder> {

    private ListViewHolder.ItemClickListener listener;

    public ItemAdapter(ListViewHolder.ItemClickListener listener){
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_list, parent, false);
        return new ListViewHolder(v, parent.getContext(), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.setObj(getItem(position));
    }

    private static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Item>() {

                @Override
                public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
                    return false;
                }
            };

}
