package com.naman.accounts.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.naman.accounts.Model.Journal;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.ExpenseListHomeAdapter;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.JournalService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ExpenseListScreen extends AppCompatActivity {

    private TextView date, opening, closing;
    private List<Journal> journalList;
    private RecyclerView rv;
    Spinner spinAccounts;
    List<String> accountsName;
    ExpenseListHomeAdapter adapter;
    double openingValue;
    String dateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list_screen);

        date = findViewById(R.id.date_expense_list);
        opening = findViewById(R.id.opening_balance_expense_list);
        rv = findViewById(R.id.recycler_expense_list);
        spinAccounts = findViewById(R.id.spinner_expense_list);
        closing = findViewById(R.id.closing_expense_list);

        Thread t = new Thread(()->{
           accountsName = DatabaseAdapter.getInstance(this).accountDao().fetchSpecificAccounts(AppConstants.AC_TYPE_EXPENSE);
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accountsName);
        spinAccounts.setAdapter(adapter);
        spinAccounts.setSelection(0);

        spinAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateValue = AppUtil.formatDate(LocalDate.now());
        date.setOnClickListener((View v)->{
            generateDatePicker();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new ExpenseListHomeAdapter();
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(layoutRv);
        rv.setAdapter(adapter);

        initValues();
    }

    private void initValues(){
        String selectedAccount = spinAccounts.getSelectedItem().toString();
        date.setText(AppUtil.formatDateToVIew(dateValue));

        Thread t = new Thread(()->{
            journalList = DatabaseAdapter.getInstance(this).journalDao().fetchTransactionsForAccountByDate(selectedAccount, dateValue);
            openingValue = new JournalService(DatabaseAdapter.getInstance(this)).calculateOpeningBalance(dateValue, selectedAccount);
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        adapter.submitList(journalList);
        opening.setText(String.valueOf(openingValue));
        closing.setText(String.valueOf(AppUtil.getValue(journalList)+ openingValue));
    }

    private void generateDatePicker(){
        LocalDate date1 = AppUtil.formatLocalDateFromString(dateValue);
        DatePickerDialog dialog = new DatePickerDialog(
                this, (DatePicker view, int year, int month, int dayOfMonth) ->{
            dateValue = AppUtil.formatDate(LocalDate.of(year, month + 1, dayOfMonth));
            initValues();
        }, date1.getYear(), date1.getMonthValue() - 1, date1.getDayOfMonth());
        dialog.show();
    }
}
