package com.naman.accounts.BottomFragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.SalaryDetailActivity;
import com.naman.accounts.screens.SalaryDisplayActivity;
import com.naman.accounts.service.AccountService;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.JournalService;


import java.time.LocalDate;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSalaryFragment extends BottomSheetDialogFragment{

    private EditText amountTxt;
    private TextView dateTxt, empName;
    private Salary salaryDb;
    private boolean someChange;
    private Spinner fromAccountSpinner;
    private List<String> accountList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.layout_bottom_salary_edit, container, false);
        amountTxt = v.findViewById(R.id.bottom_salary_edit_amount);
        dateTxt = v.findViewById(R.id.bottom_salary_edit_date);
        Button updateBtn = v.findViewById(R.id.btn_update_bottom_salary);
        fromAccountSpinner = v.findViewById(R.id.spinner_bottom_salary);
        empName = v.findViewById(R.id.account_name_bottom_salary);

        if(getArguments() != null) {
            long id = getArguments().getLong("id");
            Thread t = new Thread(() -> {
                salaryDb = DatabaseAdapter.getInstance(getContext()).salaryDao().fetchSalary(id);
                accountList = DatabaseAdapter.getInstance(getContext()).accountDao().fetchSpecificAccounts(AppConstants.AC_TYPE_EXPENSE);
            });
            t.start();
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accountList);
            fromAccountSpinner.setAdapter(adapter);

            dateTxt.setOnClickListener((View view)->{
                LocalDate date = LocalDate.now();
                new DatePickerDialog(
                        getContext(), (DatePicker x, int year, int month, int dayOfMonth) ->{
                    dateTxt.setText(AppUtil.formatDate(LocalDate.of(year, month + 1, dayOfMonth)));
                }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).show();
            });

            updateBtn.setOnClickListener((View view)->{
                someChange = true;
                if(amountTxt.getText().toString().isEmpty()){
                    amountTxt.setText("0.0");
                }
                salaryDb.setPayDate(dateTxt.getText().toString());
                salaryDb.setAmountPaid(Double.parseDouble(amountTxt.getText().toString()));
                new Thread(()->{
                    DatabaseAdapter.getInstance(getContext()).salaryDao().updateSalary(salaryDb);
                    insertSalaryPayment();
                }).start();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            });

            if(salaryDb != null && salaryDb.getId() != 0){
               if(salaryDb.getPayDate() == null || salaryDb.getPayDate().isEmpty()){
                   amountTxt.setText(String.valueOf(salaryDb.getSalaryToPay()));
                   dateTxt.setText(AppUtil.formatDate(LocalDate.now()));
               }
               else {
                   amountTxt.setText(String.valueOf(salaryDb.getAmountPaid()));
                   dateTxt.setText(salaryDb.getPayDate());
                   updateBtn.setVisibility(View.GONE);
               }

            }
            empName.setText(salaryDb.getEmpName());
        }
            return v;
    }

    private void insertSalaryPayment(){
        String account = salaryDb.getEmpName();
        String date = dateTxt.getText().toString();
        double paidAmount = Double.parseDouble(amountTxt.getText().toString());
        JournalService service = new JournalService(DatabaseAdapter.getInstance(getContext()));

        // CREDIT 'to pay' salary in employee account
        service.createJournal(account, date, false, "Salary Credit for " + salaryDb.getMonth(), salaryDb.getSalaryToPay(), null);
        //DEBIT 'paid' amount in employee account
        service.createJournal(account, date, true, "Salary Added", paidAmount, null);
        //CREDIT from 'expense account' value
        service.createJournal(fromAccountSpinner.getSelectedItem().toString(), date, false, account, paidAmount, null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getActivity() instanceof SalaryDisplayActivity){
            if(someChange){
                Thread t = new Thread(()->{
                    DatabaseAdapter.getInstance(getContext()).salaryDao().updateSalary(salaryDb);
                });
                t.start();
                try{
                    t.join();
                }catch (Exception e){
                    e.printStackTrace();
                }
                ((SalaryDisplayActivity) getActivity()).getDisplayList(((SalaryDisplayActivity) getActivity()).monthValue);
            }
        }
    }
}
