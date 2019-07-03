package com.naman.accounts.screens;

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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naman.accounts.BottomFragments.BottomExpenseFragment;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.ExpenseExpandableListAdapter;
import com.naman.accounts.adapter.ExpenseListAdapter;
import com.naman.accounts.screens.ExpenseCreationActivity;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.ExpenseListPump;
import com.naman.accounts.service.JournalService;
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
    TextView opening, closing;
    Spinner accountSpinner;
    RelativeLayout layoutBalance;
    List<String> accountList;
    double openingAmount = 0;
    double closingAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        cv = findViewById(R.id.calendar_view_expense);
        expandableListView = findViewById(R.id.expandable_expense_list);
        opening = findViewById(R.id.opening_balance_expense_list);
        closing = findViewById(R.id.closing_expense_list);
        layoutBalance = findViewById(R.id.layout_balance_expense_list);
        accountSpinner = findViewById(R.id.spinner_expense_list);
        bottomDialog();

        currentDate = AppUtil.formatDate(LocalDate.ofEpochDay(cv.getDate()/86400000L));

        cv.setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) ->{
            currentDate = AppUtil.formatDate(LocalDate.of(year, month+1, dayOfMonth));
            fillRecycler();
        });

        FloatingActionButton fab = findViewById(R.id.add_expense_fab);
        fab.setOnClickListener((View v)->{
            Intent intent = new Intent(this, ExpenseCreationActivity.class);
            intent.putExtra("date", currentDate);
            startActivity(intent);
        });

        expandableListView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) ->{
                BottomExpenseFragment frag = new BottomExpenseFragment();
                Bundle b = new Bundle();
                Journal j = adapter.getGroup(position);
                if(j != null)
                    b.putLong("id", j.getId());
                frag.setArguments(b);
                frag.show(getSupportFragmentManager(), frag.getTag());
                return false;
        });
        Thread t = new Thread(()->{
           accountList = DatabaseAdapter.getInstance(this).accountDao().fetchSpecificAccounts(AppConstants.AC_TYPE_EXPENSE);
        });
        t.start();
        try {
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        accountList.add("All");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accountList);
        accountSpinner.setAdapter(adapter);
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillRecycler();
                if(accountSpinner.getSelectedItem().toString().equalsIgnoreCase("All")){
                    layoutBalance.setVisibility(View.GONE);
                }
                else
                    layoutBalance.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRecycler();
    }

    public void fillRecycler(){
        Thread t = new Thread(()->{
            expenseMap = ExpenseListPump.returnExpenseMap(DatabaseAdapter.getInstance(this),
                    currentDate, accountSpinner.getSelectedItem().toString());
            if(!accountSpinner.getSelectedItem().toString().equalsIgnoreCase("All")){
                openingAmount = new JournalService(DatabaseAdapter.getInstance(this))
                        .calculateOpeningBalance(currentDate, accountSpinner.getSelectedItem().toString());
            }

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
            closingAmount = openingAmount;
            for(Journal j : jList){
                if(j.getType() == AppConstants.INT_CREDIT)
                    closingAmount += j.getAmount()*-1;
                else
                    closingAmount += j.getAmount();
            }
        }
        else{
            List<Journal> jList = new ArrayList<>();
            adapter = new ExpenseExpandableListAdapter(this, jList, expenseMap);
            expandableListView.setAdapter(adapter);
        }
        opening.setText(String.valueOf(openingAmount));
        closing.setText(String.valueOf(closingAmount));
    }

    private void bottomDialog(){
        View bottomSheet = findViewById(R.id.nested_bottom_expense);
        BottomSheetBehavior mBottomSheetBehavior;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
    }
}


