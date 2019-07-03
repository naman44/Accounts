package com.naman.accounts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.R;

import java.util.List;

public class HorizontalStringAdapter extends RecyclerView.Adapter<HorizontalStringAdapter.StringViewHolder> {

    private List<String> listNames;

    class StringViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;

        public StringViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.single_string_value);
        }

        public void setObj(String name){
            itemName.setText(name);
        }
    }

    public HorizontalStringAdapter(List<String> listNames){
        this.listNames = listNames;
    }

    @NonNull
    @Override
    public StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_string_value, null);
        return new StringViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
        holder.setObj(listNames.get(position));
    }

    @Override
    public int getItemCount() {
        return listNames.size();
    }

    public void updateList(List<String> newList){
        this.listNames.clear();
        listNames.addAll(newList);
        notifyDataSetChanged();
    }
}
