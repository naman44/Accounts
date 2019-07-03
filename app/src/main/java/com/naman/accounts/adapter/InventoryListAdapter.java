package com.naman.accounts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Fragments.InventoryFragment;
import com.naman.accounts.Model.Item;
import com.naman.accounts.Model.ItemInventory;
import com.naman.accounts.R;

public class InventoryListAdapter extends ListAdapter<Item, InventoryListAdapter.InventoryListHolder> {

    class InventoryListHolder extends RecyclerView.ViewHolder{

        private TextView name, salary, advance, rest, id;

        InventoryListHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.salary_list_name);
            salary = itemView.findViewById(R.id.salary_list_amount);
            advance = itemView.findViewById(R.id.salary_list_advance);
            rest = itemView.findViewById(R.id.salary_list_rest);
            id = itemView.findViewById(R.id.text_first);
        }

        void setObj(Item itemInventory){
            name.setText(itemInventory.getItemName());
            salary.setText(String.valueOf(itemInventory.getQuantity()));
            advance.setText(String.valueOf(itemInventory.getPacking()));
        }
    }

    public InventoryListAdapter(){
        super(DIFF_CALLBACK);
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

    @NonNull
    @Override
    public InventoryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_salary_list, parent, false);
        return new InventoryListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryListHolder holder, int position) {
        holder.setObj(getItem(position));
    }
}
