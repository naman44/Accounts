package com.naman.accounts.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naman.accounts.Model.Employee;
import com.naman.accounts.Model.Item;
import com.naman.accounts.R;

import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder {

    private TextView iName, iVal1, iVal2, imgText;
    ImageButton btnEdit, btnDelete;
    private ItemClickListener listener;
    public interface ItemClickListener{
        void onEditClicked(Object obj);
        void onDeleteClicked(Object obj);
    }

    ListViewHolder(View itemView, Context context, ItemClickListener listener){
        super(itemView);
        iName = itemView.findViewById(R.id.item_name);
        iVal1 = itemView.findViewById(R.id.item_value1);
        iVal2 = itemView.findViewById(R.id.item_value2);
        imgText = itemView.findViewById(R.id.text_first);
        btnEdit = itemView.findViewById(R.id.btn_edit_item);
        btnDelete = itemView.findViewById(R.id.btn_del_item);
        this.listener = listener;
    }

    void setObj(Employee e){
        iName.setText(e.getEmpName());
        iVal1.setText(e.getEmpSalary() + "");
        iVal2.setText(e.getDateOfJoin());
        if(!iName.getText().toString().isEmpty())
            imgText.setText(e.getEmpName().substring(0,1));
        btnDelete.setOnClickListener((View v)-> listener.onDeleteClicked(e));
        btnEdit.setOnClickListener((View v) -> listener.onEditClicked(e));
    }

    void setObj(Item i){
        iName.setText(i.getItemName());
        iVal1.setText(i.getItemType());
        iVal2.setText(i.getPlastic());
        if(!iName.getText().toString().isEmpty())
            imgText.setText(i.getItemName().substring(0,1));
        btnDelete.setOnClickListener((View v)-> listener.onDeleteClicked(i));
        btnEdit.setOnClickListener((View v) -> listener.onEditClicked(i));
    }
}
