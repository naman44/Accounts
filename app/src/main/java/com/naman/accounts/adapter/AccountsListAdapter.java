package com.naman.accounts.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naman.accounts.Model.Accounts;
import com.naman.accounts.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AccountsListAdapter extends ListAdapter<Accounts, AccountsListAdapter.AccountListHolder> {

    class AccountListHolder extends RecyclerView.ViewHolder{

        private TextView attName, attAdvance, timeIn, timeOut;

        AccountListHolder(View view){
            super(view);
            attName = itemView.findViewById(R.id.att_name);
            attAdvance = itemView.findViewById(R.id.att_advance);
            timeIn = itemView.findViewById(R.id.time_in);
            timeOut = itemView.findViewById(R.id.time_out);
        }

        void setObj(Accounts t){
            attName.setText(t.getName());
            attAdvance.setText(String.valueOf(t.getClosingBalance()));
        }
    }

    public AccountsListAdapter(){
        super(DIFF_CALLBACK);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListHolder holder, int position) {
        holder.setObj(getItem(position));
    }

    @NonNull
    @Override
    public AccountListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendance_list, parent, false);
        return new AccountListHolder(v);
    }

    private static final DiffUtil.ItemCallback<Accounts> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Accounts>() {

                @Override
                public boolean areItemsTheSame(@NonNull Accounts oldItem, @NonNull Accounts newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Accounts oldItem, @NonNull Accounts newItem) {
                    return false;
                }
            };

}
