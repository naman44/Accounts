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

public class SalaryListAdapter extends ListAdapter<Salary, SalaryListAdapter.SalaryListHolder> {

    private Context mContext;
    class SalaryListHolder extends RecyclerView.ViewHolder{

        private TextView name, salary, advance, rest, id, paidTxt;

        SalaryListHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.salary_list_name);
            salary = itemView.findViewById(R.id.salary_list_amount);
            advance = itemView.findViewById(R.id.salary_list_advance);
            rest = itemView.findViewById(R.id.salary_list_rest);
            id = itemView.findViewById(R.id.text_first);
            paidTxt = itemView.findViewById(R.id.salary_list_paid);
        }

        void setObj(Salary obj){
            name.setText(obj.getEmpName());
            salary.setText(mContext.getString(R.string.salary_placeholder, obj.getSalaryToPay()));
            advance.setText(mContext.getString(R.string.advance_placeholder, obj.getAdvance()));
            rest.setText(mContext.getString(R.string.rest_placeholder, obj.getRestDays()));
            id.setText(String.valueOf(obj.getId()));
            if(obj.getPayDate()!= null && !obj.getPayDate().isEmpty()){
                paidTxt.setVisibility(View.VISIBLE);
            }
            else
                paidTxt.setVisibility(View.GONE);
        }
    }

    public SalaryListAdapter(Context context){
        super(DIFF_CALLBACK);
        mContext = context;
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
        return new SalaryListHolder(v);
    }
}
