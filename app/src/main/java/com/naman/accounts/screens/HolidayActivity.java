package com.naman.accounts.screens;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.naman.accounts.Model.Holidays;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.HolidaysListAdapter;
import com.naman.accounts.service.HolidayListPump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HolidayActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    HolidaysListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<Holidays>> expandableListDetail;
    boolean longPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);
        expandableListView = findViewById(R.id.holiday_list_expandable);

        expandableListView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) ->{
            longPressed = true;
            int pos1 = ExpandableListView.getPackedPositionGroup(expandableListView.getExpandableListPosition(position));
            int pos2 = ExpandableListView.getPackedPositionChild(expandableListView.getExpandableListPosition(position));

            Holidays h = expandableListDetail.get(expandableListTitle.get(pos1)).get(pos2);
            AlertDialog.Builder alert = new AlertDialog.Builder(this).setMessage("Delete item?")
                    .setPositiveButton("Delete", ((DialogInterface dialog, int which)->{
                        Thread t = new Thread(()->{
                            DatabaseAdapter.getInstance(this).holidayDao().delete(h);
                        });
                        t.start();
                        try{
                            t.join();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        expandableListAdapter.notifyDataSetChanged();
                    }));
            alert.setPositiveButtonIcon(getDrawable(R.drawable.ic_delete_20dp));
            alert.setOnCancelListener((DialogInterface dialog)->
                    longPressed = false);
            alert.setOnDismissListener((DialogInterface dialog)->
                    longPressed = false);
            alert.show();
            return false;
        });

        expandableListView.setOnChildClickListener((ExpandableListView parent, View v, int groupPosition,
        int childPosition, long id)-> {
            if(!longPressed) {
                Holidays h = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(h.getDate());

                final EditText input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                input.setLayoutParams(lp);
                input.setText(h.getType());
                alert.setView(input);

                alert.setPositiveButton("Ok",
                        (DialogInterface dialog, int which) -> {
                            Thread t = new Thread(() -> {
                                h.setType(input.getText().toString());
                                DatabaseAdapter.getInstance(this).holidayDao().update(h);
                            });
                            t.start();
                            try {
                                t.join();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            expandableListAdapter.notifyDataSetChanged();
                        }).show();
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        longPressed = false;
        super.onResume();
        Thread t = new Thread(()->{
            expandableListDetail = HolidayListPump.getHolidaysList(DatabaseAdapter.getInstance(this));
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(expandableListDetail != null && expandableListDetail.size() > 0){
            expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
            expandableListAdapter = new HolidaysListAdapter(this, expandableListTitle,
                    expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);
        }
    }


}
