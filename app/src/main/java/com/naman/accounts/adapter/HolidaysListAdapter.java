package com.naman.accounts.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.naman.accounts.Model.Holidays;
import com.naman.accounts.R;

import java.util.HashMap;
import java.util.List;

public class HolidaysListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Holidays>> expandableListDetail;

    public HolidaysListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<Holidays>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Holidays getChild(int groupPosition, int childPosition) {
        return expandableListDetail.get(expandableListTitle
                .get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Holidays expandedList = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_holiday_list_item, null);
        }
        TextView expandedDate = convertView.findViewById(R.id.holiday_item_date);
        TextView expandedText = convertView.findViewById(R.id.holiday_item_type);
        TextView expandedId = convertView.findViewById(R.id.holiday_item_id);
        expandedDate.setText(expandedList.getDate());
        expandedText.setText(expandedList.getType());
        expandedId.setText(String.valueOf(expandedList.getId()));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return expandableListTitle.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return expandableListDetail.size();
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
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_expandable_title, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.holiday_list_title);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }
}
