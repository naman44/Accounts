package com.naman.accounts.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naman.accounts.Model.Journal;
import com.naman.accounts.R;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;

import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ExpenseListAdapter extends ListAdapter<Journal, ExpenseListAdapter.ExpenseListHolder> {

    class ExpenseListHolder extends RecyclerView.ViewHolder{

        private TextView attName, timeIn, timeOut;

        ExpenseListHolder(View view){
            super(view);
            attName = itemView.findViewById(R.id.att_name);
            timeIn = itemView.findViewById(R.id.time_in);
            timeOut = itemView.findViewById(R.id.time_out);
        }

        void setObj(Journal t){
            attName.setText(t.getAccountName());
            timeIn.setText(t.getRemark());
            if (t.getType() == AppConstants.INT_CREDIT){
                timeIn.setText(String.valueOf(t.getAmount()));
                timeIn.setTextColor(Color.RED);
            }
            else{
                timeIn.setText(String.valueOf(t.getAmount()));
                timeIn.setTextColor(Color.GREEN);
            }
        }
    }

    public ExpenseListAdapter(){
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListHolder holder, int position) {
        holder.setObj(getItem(position));
    }

    @NonNull
    @Override
    public ExpenseListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendance_list, parent, false);
        return new ExpenseListHolder(v);
    }

    private static final DiffUtil.ItemCallback<Journal> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Journal>() {

                @Override
                public boolean areItemsTheSame(@NonNull Journal oldItem, @NonNull Journal newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Journal oldItem, @NonNull Journal newItem) {
                    return false;
                }
            };

}
