package com.naman.accounts.BottomFragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.naman.accounts.Model.Attendance;
import com.naman.accounts.Model.AttendanceListViewModel;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.SalaryDetailActivity;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.SalaryService;

import java.time.LocalTime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class BottomFragment extends BottomSheetDialogFragment {

    private TextView name, timeIn, timeOut;
    private ImageButton timeInBtn, timeOutBtn;
    private Button saveBtn;
    private Attendance att;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.layout_attendance_edit, container, false);
        name = v.findViewById(R.id.name_att_edit);
        timeIn = v.findViewById(R.id.time_in_edit);
        timeOut = v.findViewById(R.id.time_out_edit);
        timeInBtn = v.findViewById(R.id.time_in_edit_btn);
        timeOutBtn = v.findViewById(R.id.time_out_edit_btn);
        saveBtn = v.findViewById(R.id.save_att_edit);

        if(getArguments() != null){
            String date = getArguments().getString("date");
            String empName = getArguments().getString("employee");
            Thread t = new Thread(()->{
                att = DatabaseAdapter.getInstance(getContext()).attendanceDao().
                        fetchAttendanceOfEmployeeByDate(date, empName);
            });
            t.start();
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        assignValuesToView(att);
        return v;
    }

    private void assignValuesToView(Attendance attendance){
        name.setText(attendance.getEmpName());
        timeIn.setText(attendance.getTimeIn());
        timeOut.setText(attendance.getTimeOut());
        if(attendance.getPresent() == 0){
            name.setTextColor(Color.RED);
        }

        timeIn.setOnClickListener((View v)->
            openTimePicker(timeIn, timeIn.getText().toString()));
        timeOut.setOnClickListener((View v)->
            openTimePicker(timeOut, timeOut.getText().toString()));

        name.setOnClickListener((View v)->{
            if(attendance.getPresent() == 1){
                name.setTextColor(Color.RED);
                attendance.setPresent(0);
                timeIn.setText("-");
                timeOut.setText("-");
            }
            else {
                TypedValue tv = new TypedValue();
                Resources.Theme theme = getActivity().getTheme();
                boolean suceess = theme.resolveAttribute(R.attr.navigationTextColor, tv, true);
                int color = 0;
                if(suceess)
                    color = tv.data;
                if(color == 0)
                    name.setTextColor(getResources().getColor(R.color.colorAccent, theme));
                else
                    name.setTextColor(color);
                attendance.setPresent(1);
                timeIn.setText(AppConstants.startingTime);
                timeOut.setText(AppConstants.endTime);
            }
        });

        saveBtn.setOnClickListener((View v)->{
            //Save to DB
            attendance.setTimeIn(timeIn.getText().toString());
            attendance.setTimeOut(timeOut.getText().toString());

            AttendanceListViewModel model = ViewModelProviders.of(this).get(AttendanceListViewModel.class);
            model.updateAttendance(attendance);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
    }

    private void openTimePicker(TextView viewTxt, String time){
        LocalTime timeLocal = AppUtil.formatLocalTimeFromString(time);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), (TimePicker timePicker, int selectedHour, int selectedMinute)->
            viewTxt.setText(String.format(AppUtil.getLocale(getActivity()), "%02d:%02d", selectedHour, selectedMinute)),
                timeLocal.getHour(), timeLocal.getMinute(), true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Activity act = getActivity();
        if(act instanceof SalaryDetailActivity){
            Thread t = new Thread(()->{
                String month = AppUtil.formatYearMonth(att.getDate(), "yyyy/MM");
                new SalaryService(DatabaseAdapter.getInstance(
                        act.getApplicationContext())).updateSalarySingle(att.getEmpName(), month);
            });
            t.start();
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
            ((SalaryDetailActivity) act).fillDetails();
        }
    }
}
