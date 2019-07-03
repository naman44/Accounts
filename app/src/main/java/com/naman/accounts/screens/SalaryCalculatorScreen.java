package com.naman.accounts.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naman.accounts.R;

public class SalaryCalculatorScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_calculator_screen);

        TextView dailyWage = findViewById(R.id.daily_wage);
        TextView hourlyWage = findViewById(R.id.hourly_wage);
        TextView salaryOutput = findViewById(R.id.salary_output);
        EditText salaryInput = findViewById(R.id.salary_calc);
        EditText attendanceInput = findViewById(R.id.attendance_calc);
        EditText restInput = findViewById(R.id.rest_calc);
        EditText penaltyInput = findViewById(R.id.penalty_calc);
        Button calculate = findViewById(R.id.calculate_salary_btn);
        EditText advanceInput = findViewById(R.id.advance_calc);

        calculate.setOnClickListener((View v)->{
            if(!salaryInput.getText().toString().isEmpty() &&
                    !attendanceInput.getText().toString().isEmpty()){
                double advance = 0;
                double penalty = 0;
                int attendance = Integer.parseInt(attendanceInput.getText().toString());
                double salary = Double.parseDouble(salaryInput.getText().toString());
                if(!penaltyInput.getText().toString().isEmpty())
                    penalty = Double.parseDouble(penaltyInput.getText().toString());
                if(!advanceInput.getText().toString().isEmpty())
                    advance = Double.parseDouble(advanceInput.getText().toString());
                int rest = Integer.parseInt(restInput.getText().toString());
                double dayWage = salary/30;
                double hourRate = dayWage/8;

                dailyWage.setText(String.format("%.2f", dayWage));
                hourlyWage.setText(String.format("%.2f", hourRate));

                double salaryCalc = (attendance+rest)*dayWage + (penalty*hourRate) - advance;
                salaryOutput.setText(String.format("%.2f", salaryCalc));
            }
        });
    }
}
