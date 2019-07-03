package com.naman.accounts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.R;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.Inflater;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ExpenseExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Journal> journals;
    private LinkedHashMap<Journal, List<SubTransaction>> expenseMap;

    public ExpenseExpandableListAdapter(Context context, List<Journal> list,
                                        LinkedHashMap<Journal, List<SubTransaction>> map){
        this.context = context;
        this.journals = list;
        this.expenseMap = map;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupCount() {
        return expenseMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expenseMap.get(journals.get(groupPosition)).size();
    }

    @Override
    public Journal getGroup(int groupPosition) {
        return journals.get(groupPosition);
    }

    @Override
    public SubTransaction getChild(int groupPosition, int childPosition) {
        return expenseMap.get(journals.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.expense_list_main, null);
        }
        TextView remark = convertView.findViewById(R.id.expense_name_head_list);
        TextView account = convertView.findViewById(R.id.account_name_head_list);
        TextView amount = convertView.findViewById(R.id.expense_amount_head_list);
        ImageView indicator = convertView.findViewById(R.id.head_list_indicator);
        Journal j = getGroup(groupPosition);

        if(isExpanded || (expenseMap.get(j) == null || expenseMap.get(j).isEmpty())){
            indicator.setVisibility(View.GONE);
        }
        else{
            indicator.setVisibility(View.VISIBLE);
        }

        if(j.getRemark() == null || j.getRemark().isEmpty()){
            remark.setText(j.getAccountName());
            account.setVisibility(View.GONE);
        }
        else{
            remark.setText(j.getRemark());
            account.setText(j.getAccountName());
            account.setVisibility(View.VISIBLE);
        }

        amount.setTypeface(null, Typeface.BOLD);
        if(j.getType() == AppConstants.INT_CREDIT){
            amount.setText("-" + j.getAmount());
            amount.setTextColor(context.getColor(R.color.red));
        }
        else{
            amount.setText("+" + j.getAmount());
            amount.setTextColor(context.getColor(R.color.colorAccentGreen));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_attendance_list, null);
        }
        TextView attName = convertView.findViewById(R.id.time_in);
        TextView attAdvance = convertView.findViewById(R.id.time_out);

        SubTransaction sbt = getChild(groupPosition, childPosition);
        if(sbt != null){
            attName.setText(sbt.getName());
            attAdvance.setText(String.valueOf(sbt.getAmount()));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
