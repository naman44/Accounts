package com.naman.accounts.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naman.accounts.Model.Employee;
import com.naman.accounts.R;

import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder {

    private TextView iName, iVal1, iVal2, imgText;
    private Employee e;
    public interface ItemClickListener{
        void onEditClicked(Employee obj);
        void onDeleteClicked(Employee obj);
    }

    ListViewHolder(View itemView, Context context, ItemClickListener listener){
        super(itemView);
        iName = itemView.findViewById(R.id.item_name);
        iVal1 = itemView.findViewById(R.id.item_value1);
        iVal2 = itemView.findViewById(R.id.item_value2);
        imgText = itemView.findViewById(R.id.text_first);
        ImageButton btnEdit = itemView.findViewById(R.id.btn_edit_item);
        ImageButton btnDelete = itemView.findViewById(R.id.btn_del_item);
        if(listener == null){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
        else{
            btnDelete.setOnClickListener((View v)-> listener.onDeleteClicked(e));
            btnEdit.setOnClickListener((View v) -> listener.onEditClicked(e));
        }
    }

    void setObj(Employee e){
        this.e = e;
        iName.setText(e.getEmpName());
        iVal1.setText(e.getEmpSalary() + "");
        iVal2.setText(e.getDateOfJoin());
        if(!iName.getText().toString().isEmpty())
            imgText.setText(e.getEmpName().substring(0,1));
    }
}
