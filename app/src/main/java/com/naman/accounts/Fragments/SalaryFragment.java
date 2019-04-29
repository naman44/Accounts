package com.naman.accounts.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.naman.accounts.BottomFragments.BottomFragment;
import com.naman.accounts.Model.Attendance;
import com.naman.accounts.Model.AttendanceListViewModel;
import com.naman.accounts.Model.Holidays;
import com.naman.accounts.R;
import com.naman.accounts.adapter.AttendanceListAdapter;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.AddEmployee;
import com.naman.accounts.screens.HolidayActivity;
import com.naman.accounts.screens.SalaryDisplayActivity;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.AttendanceService;
import com.naman.accounts.service.RecyclerItemClickListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SalaryFragment extends Fragment {

    private Button empBtn, salaryBtn, attendanceBtn, holidaysBtn;
    private ImageView dotImage;
    private RecyclerView homeRv;
    private TextView dateView, msgMainPage;
    private AttendanceListAdapter adapter;
    private List<Attendance> aList;
    private AttendanceListViewModel model;
    private DatePickerDialog dialog;
    private CheckBox checkHoliday;
    private TranslateAnimation animation;
    private View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.content_salary_page, null);
        initializeViews();
        setListeners();
        dateView.setText(AppUtil.formatDate(LocalDate.now()));
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        generateListForAttendance();
    }

    private void initializeViews(){
        empBtn = mainView.findViewById(R.id.emp_btn_home);
        salaryBtn = mainView.findViewById(R.id.sal_btn_home);
        attendanceBtn = mainView.findViewById(R.id.att_btn_home);
        dateView = mainView.findViewById(R.id.att_date);
        homeRv = mainView.findViewById(R.id.recycler_home);
        checkHoliday = mainView.findViewById(R.id.checkBox_holiday);
        msgMainPage = mainView.findViewById(R.id.msg_main_txt);
        dotImage = mainView.findViewById(R.id.dot_txt_main);
        holidaysBtn = mainView.findViewById(R.id.holidays_btn);

        initializeRecycler();
        bottomDialog();
        defineAnimation();
    }

    private void bottomDialog(){
        View bottomSheet = mainView.findViewById(R.id.home_bottom_sheet);
        BottomSheetBehavior mBottomSheetBehavior;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
    }

    private void setListeners(){
        empBtn.setOnClickListener((View v)-> startActivity(new Intent(getActivity(), AddEmployee.class)));

        attendanceBtn.setOnClickListener((View v)-> {
            if (aList == null || aList.size() == 0) {
                new Thread(() ->
                        new AttendanceService().generateAttendance(dateView.getText().toString(), getContext())).start();
            }
            else{
                displayToggle(-1);
            }
        });

        salaryBtn.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), SalaryDisplayActivity.class));
        });

        checkHoliday.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked)-> {
            if(isChecked){
                Holidays h = new Holidays();
                h.setDate(dateView.getText().toString());
                h.setType("Anonymous");
                h.setMonth(AppUtil.formatYearMonth(dateView.getText().toString(), "yyyy/MM"));

                model.deleteAttendance(dateView.getText().toString());
                new Thread(()-> DatabaseAdapter.getInstance(getContext()).holidayDao().insertHoliday(h)).start();
                generateListForAttendance();
            }
        });

        dateView.setOnClickListener((View v)->{
            if(dateView.getText().toString().startsWith("2")){
                generateDatePicker();
                dialog.show();
            }
        });

        holidaysBtn.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), HolidayActivity.class));
        });
    }

    /**
     * method to toggle the view of attendance on page according to use
     * @param toggle ON/OFF
     */
    private void toggleAttendance(String toggle){
        if(toggle.equalsIgnoreCase("ON")){
            mainView.findViewById(R.id.view2).setVisibility(View.VISIBLE);
            mainView.findViewById(R.id.view3).setVisibility(View.VISIBLE);
            checkHoliday.setVisibility(View.VISIBLE);
            msgMainPage.setVisibility(View.GONE);
            dotImage.setVisibility(View.GONE);
        }
        else{
            mainView.findViewById(R.id.view2).setVisibility(View.GONE);
            mainView.findViewById(R.id.view3).setVisibility(View.GONE);
            checkHoliday.setVisibility(View.GONE);
            msgMainPage.setVisibility(View.VISIBLE);
            dotImage.setVisibility(View.VISIBLE);
        }
    }

    private void initializeRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(getContext());
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        homeRv.setLayoutManager(layoutRv);
        adapter = new AttendanceListAdapter();
        homeRv.setAdapter(adapter);
        homeRv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                (View view, int position)-> {
                    BottomFragment frag = new BottomFragment();
                    Bundle b = new Bundle();
                    b.putString("date", aList.get(position).getDate());
                    b.putString("employee", aList.get(position).getEmpName());
                    frag.setArguments(b);
                    frag.show(getFragmentManager(), frag.getTag());
                }));
        aList = new ArrayList<>();
    }

    private void generateListForAttendance() {
        model = ViewModelProviders.of(this).get(AttendanceListViewModel.class);
        model.getAttendanceList(dateView.getText().toString()).observe(this, (List<Attendance> attendances)-> {
            aList = attendances;
            adapter.submitList(aList);
            displayToggle(model.getModeValue());
        });
    }

    /**
     * display settings
     * 0 = no employee found in record for attendance
     * 1 = holiday defined on this day
     * 2 = Sunday
     * @param value 0-2
     */
    private void displayToggle(int value){
        switch (value){
            case 0:
                mainView.findViewById(R.id.view3).startAnimation(animation);
                msgMainPage.setText(getString(R.string.msg_main_no_employee));
                break;
            case 1:
                mainView.findViewById(R.id.view3).startAnimation(animation);
                msgMainPage.setText(getString(R.string.msg_main_holiday));
                break;
            case 2:
                mainView.findViewById(R.id.view3).startAnimation(animation);
                msgMainPage.setText(getString(R.string.msg_main_sunday));
                break;
            default:
                toggleAttendance("ON");
                break;
        }
    }

    private void generateDatePicker(){
        LocalDate date = AppUtil.formatLocalDateFromString(dateView.getText().toString());
        dialog = new DatePickerDialog(
                getContext(), (DatePicker view, int year, int month, int dayOfMonth) ->{
            dateView.setText(AppUtil.formatDate(LocalDate.of(year, month + 1, dayOfMonth)));
            generateListForAttendance();
            checkHoliday.setChecked(false);
        }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
    }

    private void defineAnimation(){
        animation = new TranslateAnimation(
                0,0,0, mainView.findViewById(R.id.view3).getHeight());
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                toggleAttendance("OFF");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainView.findViewById(R.id.view3).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
