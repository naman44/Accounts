package com.naman.accounts.screens;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.naman.accounts.Model.Employee;
import com.naman.accounts.Model.EmployeeListViewModel;
import com.naman.accounts.R;
import com.naman.accounts.adapter.EmployeeAdapter;
import com.naman.accounts.adapter.ListViewHolder;
import com.naman.accounts.service.AppUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddEmployee extends AppCompatActivity implements ListViewHolder.ItemClickListener, View.OnClickListener {

    TextInputLayout layoutName, layoutSal;
    TextInputEditText nameText, salaryText;
    RecyclerView empRv;
    TextView doj, lastDay;
    EmployeeAdapter adapter;
    EmployeeListViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        initiateViews();
        initiateRecycler();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
        resetView();
    }

    private void loadList(){
        model.getEmployeeList().observe(this, (List<Employee> employees)->
            adapter.updateList(employees));
    }

    private void initiateViews(){
        layoutName = findViewById(R.id.layoutName);
        layoutSal = findViewById(R.id.layoutSalary);
        nameText = findViewById(R.id.name_text);
        salaryText = findViewById(R.id.sal_text);
        empRv = findViewById(R.id.recycler_employee);
        doj = findViewById(R.id.doj_value);
        lastDay = findViewById(R.id.last_day_value);
        model = ViewModelProviders.of(this).get(EmployeeListViewModel.class);
    }

    private void initiateRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        empRv.setLayoutManager(layoutRv);
        adapter = new EmployeeAdapter(this, new ArrayList<>(1), this);
        empRv.setAdapter(adapter);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setListeners(){
        // FAB
        findViewById(R.id.fab).setOnClickListener((View view)-> {
            // save to DB, if name is not null
            if(nameText.getText() != null && !nameText.getText().toString().isEmpty()){
                layoutName.setErrorEnabled(false);
                Employee e = getEmployeeFromUser();
                if(e != null){
                    model.upSert(e);
                    resetView();
                    hideKeyboard();
                }
            }
            else {
                layoutName.setErrorEnabled(true);
                nameText.setError("Name required!!!");
            }
        });
        doj.setOnClickListener(this);
        lastDay.setOnClickListener(this);
    }

    private Employee getEmployeeFromUser(){
        Employee e = new Employee();
        if(nameText == null || nameText.getText() == null){
            return null;
        }
        e.setEmpName(nameText.getText().toString());
        if(salaryText.getText() != null && !salaryText.getText().toString().isEmpty()){
            e.setEmpSalary(Double.parseDouble(salaryText.getText().toString()));
        }
        e.setDateOfJoin(doj.getText().toString());
        e.setLastDate(lastDay.getText().toString());
        return e;
    }

    private void resetView(){
        nameText.setText("");
        salaryText.setText("");
        doj.setText(AppUtil.formatDate(LocalDate.now()));
        lastDay.setText(getString(R.string.text_working));
    }

    @Override
    public void onClick(View v) {
        new DatePickerDialog(this, (datePicker, year, month, date) ->
            ((TextView)v).setText(AppUtil.formatDate(LocalDate.of(year, month + 1, date)))
        , LocalDate.now().getYear(),LocalDate.now().getMonthValue() - 1,LocalDate.now().getDayOfMonth()).show();
    }

    @Override
    public void onEditClicked(Employee obj) {
        nameText.setText(obj.getEmpName());
        salaryText.setText(String.valueOf(obj.getEmpSalary()));
        doj.setText(obj.getDateOfJoin());
        lastDay.setText(obj.getLastDate());
    }

    @Override
    public void onDeleteClicked(Employee obj) {
        model.deleteEmployee(obj);
    }
}
