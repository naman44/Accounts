package com.naman.accounts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naman.accounts.BottomFragments.BottomExpenseFragment;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.ExpenseExpandableListAdapter;
import com.naman.accounts.adapter.ExpenseListAdapter;
import com.naman.accounts.screens.ExpenseCreationActivity;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.ExpenseListPump;
import com.naman.accounts.service.RecyclerItemClickListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    CalendarView cv;
    ExpandableListView expandableListView;
    ExpenseExpandableListAdapter adapter;
    LinkedHashMap<Journal, List<SubTransaction>> expenseMap;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        cv = findViewById(R.id.calendar_view_expense);
        expandableListView = findViewById(R.id.expandable_expense_list);
        bottomDialog();

        currentDate = AppUtil.formatDate(LocalDate.ofEpochDay(cv.getDate()/86400000L));

        cv.setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) ->{
            currentDate = AppUtil.formatDate(LocalDate.of(year, month+1, dayOfMonth));
            fillRecycler();
        });

        FloatingActionButton fab = findViewById(R.id.add_expense_fab);
        fab.setOnClickListener((View v)->{
            Intent intent = new Intent(this, ExpenseCreationActivity.class);
            //currentDate = AppUtil.formatDate(LocalDate.ofEpochDay(cv.getDate()/86400000L));
            intent.putExtra("date", currentDate);
            startActivity(intent);
        });

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BottomExpenseFragment frag = new BottomExpenseFragment();
                Bundle b = new Bundle();
                Journal j = adapter.getGroup(position);
                if(j != null)
                    b.putLong("id", j.getId());
                frag.setArguments(b);
                frag.show(getSupportFragmentManager(), frag.getTag());
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //String date = AppUtil.formatDate(LocalDate.ofEpochDay(cv.getDate()/86400000L));
        fillRecycler();
    }

    public void fillRecycler(){
        Thread t = new Thread(()->{
            expenseMap = ExpenseListPump.returnExpenseMap(DatabaseAdapter.getInstance(this),
                    currentDate);
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(expenseMap != null && expenseMap.size() > 0){
            List<Journal> jList = new ArrayList<>(expenseMap.keySet());
            adapter = new ExpenseExpandableListAdapter(this, jList, expenseMap);
            expandableListView.setAdapter(adapter);
        }
        else{
            List<Journal> jList = new ArrayList<>();
            adapter = new ExpenseExpandableListAdapter(this, jList, expenseMap);
            expandableListView.setAdapter(adapter);
        }
    }

    private void bottomDialog(){
        View bottomSheet = findViewById(R.id.nested_bottom_expense);
        BottomSheetBehavior mBottomSheetBehavior;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
    }
}


