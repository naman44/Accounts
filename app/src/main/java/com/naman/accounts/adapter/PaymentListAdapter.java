package com.naman.accounts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Model.Payments;
import com.naman.accounts.R;

public class PaymentListAdapter extends ListAdapter<Payments, PaymentListAdapter.PaymentListHolder> {

    class PaymentListHolder extends RecyclerView.ViewHolder {

        private TextView t1, t2, t3;

        PaymentListHolder(View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.att_name);
            t2 = itemView.findViewById(R.id.time_in);
            t3 = itemView.findViewById(R.id.time_out);
        }

        public void setObj(Payments payments){
            t1.setText(payments.getDate());
            t2.setText(String.valueOf(payments.getAmount()));
            t3.setText(payments.getRemark());
        }
    }

    public PaymentListAdapter(){
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PaymentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendance_list, parent, false);
        return new PaymentListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentListHolder holder, int position) {
        holder.setObj(getItem(position));
    }

    private static final DiffUtil.ItemCallback<Payments> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Payments>() {

                @Override
                public boolean areItemsTheSame(@NonNull Payments oldItem, @NonNull Payments newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Payments oldItem, @NonNull Payments newItem) {
                    return false;
                }
            };
}
