package com.naman.accounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Model.Journal;
import com.naman.accounts.R;
import com.naman.accounts.service.AppConstants;

public class ExpenseListHomeAdapter extends ListAdapter<Journal, ExpenseListHomeAdapter.ExpenseListHolder> {

    private Context mContext;

    class ExpenseListHolder extends RecyclerView.ViewHolder {

        private TextView date, remark, amount;
        private LinearLayout layout;
        ExpenseListHolder(View view){
            super(view);
            amount = view.findViewById(R.id.amount_expense_list_item);
            date = view.findViewById(R.id.date_expense_list_item);
            remark = view.findViewById(R.id.remark_expense_list_item);
            layout = view.findViewById(R.id.layout_expense_list_item);
        }

        public void setObj(Journal j){
            amount.setText(String.valueOf(j.getAmount()));
            date.setText(j.getDate());

            if(j.getType() == AppConstants.INT_CREDIT){
                final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
                params.gravity = GravityCompat.END;
                layout.setLayoutParams(params);
                layout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_minus));
                remark.setText(mContext.getString(R.string.arrow_placeholder, j.getAccountName(), j.getRemark()));
            }
            else{
                layout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_plus));
                remark.setText(mContext.getString(R.string.arrow_placeholder, j.getRemark(), j.getAccountName()));
            }

        }
    }

    public ExpenseListHomeAdapter(){
        super(DIFF_CALLBACK);
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

    @NonNull
    @Override
    public ExpenseListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_expense_list, parent, false);
        mContext = parent.getContext();
        return new ExpenseListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListHolder holder, int position) {
        holder.setObj(getItem(position));
    }
}
