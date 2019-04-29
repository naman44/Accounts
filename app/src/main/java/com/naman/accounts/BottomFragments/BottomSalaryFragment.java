package com.naman.accounts.BottomFragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.SalaryDetailActivity;
import com.naman.accounts.service.AppUtil;


import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSalaryFragment extends BottomSheetDialogFragment implements TextWatcher{

    private EditText amountTxt;
    private TextView dateTxt, warningTxt;
    Button updateBtn;
    Salary salaryDb;
    boolean someChange;

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
        updateBtn = v.findViewById(R.id.btn_update_bottom_salary);
        warningTxt = v.findViewById(R.id.bottom_salary_warning);
        warningTxt.setVisibility(View.GONE);

        if(getArguments() != null) {
            long id = getArguments().getLong("id");
            Thread t = new Thread(() -> {
                salaryDb = DatabaseAdapter.getInstance(getContext()).salaryDao().fetchSalary(id);
            });
            t.start();
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(salaryDb != null && salaryDb.getId() != 0){
                amountTxt.setText(String.valueOf(salaryDb.getAmountPaid()));
                dateTxt.setText(salaryDb.getPayDate());
                if(salaryDb.getPayDate() == null || salaryDb.getPayDate().isEmpty())
                    dateTxt.setText("Input Date");
            }
        }

        dateTxt.setOnClickListener((View view)->{
            String dateString;
            if(salaryDb.getPayDate() == null || salaryDb.getPayDate().isEmpty()){
                dateString = AppUtil.formatDate(LocalDate.now());
            }
            else
                dateString = dateTxt.getText().toString();
            LocalDate date = AppUtil.formatLocalDateFromString(dateString);
            new DatePickerDialog(
                    getContext(), (DatePicker x, int year, int month, int dayOfMonth) ->{
                dateTxt.setText(AppUtil.formatDate(LocalDate.of(year, month + 1, dayOfMonth)));
            }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).show();
        });

        dateTxt.addTextChangedListener(this);
        amountTxt.addTextChangedListener(this);

        updateBtn.setOnClickListener((View view)->{
            if(dateTxt.getText().toString().equalsIgnoreCase("Input Date")){
                dateTxt.setText("");
            }
            if(amountTxt.getText().toString().isEmpty()){
                amountTxt.setText("0.0");
            }
            if(salaryDb.getAmountPaid() == Double.parseDouble(amountTxt.getText().toString())
            && ifDateSame()){
                Toast.makeText(getContext(), "No Change", Toast.LENGTH_SHORT).show();
            }
            else{
                someChange = true;
                salaryDb.setPayDate(dateTxt.getText().toString());
                salaryDb.setAmountPaid(Double.parseDouble(amountTxt.getText().toString()));
                new Thread(()->{
                    DatabaseAdapter.getInstance(getContext()).salaryDao().updateSalary(salaryDb);
                });
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
        });
            return v;
    }

    private boolean ifDateSame(){
        if((salaryDb.getPayDate() == null || salaryDb.getPayDate().isEmpty()) &&
            dateTxt.getText().toString().isEmpty()){
            return true;
        }
        else if(salaryDb.getPayDate() != null && salaryDb.getPayDate().equalsIgnoreCase(dateTxt.getText().toString())){
            return true;
        }
        else
            return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(salaryDb.getPayDate() == null || salaryDb.getPayDate().isEmpty()){
            try{
                int x = Integer.parseInt(String.valueOf(s.charAt(0)));
                if(x > 0){
                    warningTxt.setVisibility(View.VISIBLE);
                }
                else
                    warningTxt.setVisibility(View.GONE);
            }catch (Exception e){
                warningTxt.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getActivity() instanceof SalaryDetailActivity){
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
                ((SalaryDetailActivity) getActivity()).fillDetails();
            }
        }
    }
}
