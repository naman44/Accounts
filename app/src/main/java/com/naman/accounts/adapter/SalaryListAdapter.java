package com.naman.accounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naman.accounts.Model.Salary;
import com.naman.accounts.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class SalaryListAdapter extends ListAdapter<Salary, SalaryListHolder> {

    private SalaryListHolder.mySalaryTouchListener listener;
    public SalaryListAdapter(SalaryListHolder.mySalaryTouchListener listener){
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryListHolder holder, int position) {
        holder.setObj(getItem(position));
    }

    private static final DiffUtil.ItemCallback<Salary> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Salary>() {

                @Override
                public boolean areItemsTheSame(@NonNull Salary oldItem, @NonNull Salary newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Salary oldItem, @NonNull Salary newItem) {
                    return false;
                }
            };

    @NonNull
    @Override
    public SalaryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_salary_list, parent, false);
        return new SalaryListHolder(v, parent.getContext(), listener);
    }
}
