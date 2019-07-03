package com.naman.accounts.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Model.Salary;
import com.naman.accounts.R;

public class SalaryListHolder extends RecyclerView.ViewHolder {

        private TextView name, salary, advance, rest, id;
        private mySalaryTouchListener listener;
        private Button payBtn;
        private ImageButton detailBtn;
        private Context mContext;

        public interface mySalaryTouchListener{
            void onPayClicked(long id);
            void onDetailClicked(long id);
        }

        SalaryListHolder(View itemView, Context mContext, mySalaryTouchListener listener){
            super(itemView);
            name = itemView.findViewById(R.id.salary_list_name);
            salary = itemView.findViewById(R.id.salary_list_amount);
            advance = itemView.findViewById(R.id.salary_list_advance);
            rest = itemView.findViewById(R.id.salary_list_rest);
            id = itemView.findViewById(R.id.text_first);
            payBtn = itemView.findViewById(R.id.salary_list_pay_btn);
            detailBtn = itemView.findViewById(R.id.salary_list_detail_btn);
            this.mContext = mContext;
            this.listener = listener;
        }

        void setObj(Salary obj){
            name.setText(obj.getEmpName());
            salary.setText(mContext.getString(R.string.salary_placeholder, obj.getSalaryToPay()));
            advance.setText(mContext.getString(R.string.advance_placeholder, obj.getAdvance()));
            rest.setText(mContext.getString(R.string.rest_placeholder, obj.getRestDays()));
            id.setText(String.valueOf(obj.getId()));
            if(obj.getPayDate() != null && !obj.getPayDate().isEmpty()){
                payBtn.setText("Paid");
            }
            payBtn.setOnClickListener((View v)->listener.onPayClicked(obj.getId()));
            detailBtn.setOnClickListener((View v)->listener.onDetailClicked(obj.getId()));
        }
    }
