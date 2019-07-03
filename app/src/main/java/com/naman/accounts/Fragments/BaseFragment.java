package com.naman.accounts.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.naman.accounts.R;
import com.naman.accounts.screens.ExpenseListScreen;
import com.naman.accounts.screens.SalaryCalculatorScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, null);

        Button expenseListBtn = view.findViewById(R.id.expense_list_btn_main);
        Button salaryCalcBtn = view.findViewById(R.id.salary_calc_main);
        expenseListBtn.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), ExpenseListScreen.class));
        });
        salaryCalcBtn.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), SalaryCalculatorScreen.class));
        });
        return view;
    }
}
