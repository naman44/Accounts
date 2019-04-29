package com.naman.accounts.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.naman.accounts.BottomFragments.BottomFragment;
import com.naman.accounts.BottomFragments.BottomSalaryFragment;
import com.naman.accounts.Model.Attendance;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.R;
import com.naman.accounts.adapter.AttendanceListAdapter;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.RecyclerItemClickListener;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SalaryDetailActivity extends AppCompatActivity {

    Salary salary;
    TextView salaryTxt, restTxt, presentTxt, absentTxt, advanceTxt, penaltyTxt, diffTxt, payDateTxt, toPayTxt, paidTxt;
    RecyclerView detailRv;
    AttendanceListAdapter adapter;
    List<Attendance> attendanceList;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        salaryTxt = findViewById(R.id.detail_salary);
        restTxt = findViewById(R.id.detail_rest_days);
        presentTxt = findViewById(R.id.detail_attendance);
        absentTxt = findViewById(R.id.detail_absent);
        advanceTxt = findViewById(R.id.detail_advance);
        penaltyTxt = findViewById(R.id.detail_penalty);
        diffTxt = findViewById(R.id.detail_diff);
        payDateTxt = findViewById(R.id.detail_pay_date);
        toPayTxt = findViewById(R.id.detail_toPay);
        paidTxt = findViewById(R.id.detail_paid);
        detailRv = findViewById(R.id.recycler_salary_detail);
        coordinatorLayout = findViewById(R.id.salary_detail_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view)-> {
                BottomSalaryFragment frag = new BottomSalaryFragment();
                Bundle b = new Bundle();
                b.putLong("id", salary.getId());
                frag.setArguments(b);
                frag.show(getSupportFragmentManager(), frag.getTag());
        });
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initiateRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //attendanceList = new ArrayList<>();
        fillDetails();

    }

    private void initiateRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        detailRv.setLayoutManager(layoutRv);
        adapter = new AttendanceListAdapter();
        detailRv.setAdapter(adapter);
        bottomDialog();
        detailRv.addOnItemTouchListener(new RecyclerItemClickListener(this,
                (View view, int position)-> {
                    if(salary.getPayDate() != null && !salary.getPayDate().isEmpty()){
                        Snackbar.make(view, "Salary Paid. Can't alter now!", Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        BottomFragment frag = new BottomFragment();
                        Bundle b = new Bundle();
                        b.putString("date", attendanceList.get(position).getDate());
                        b.putString("employee", salary.getEmpName());
                        frag.setArguments(b);
                        frag.show(getSupportFragmentManager(), frag.getTag());
                    }
                }));
    }

    public void fillDetails(){
        long id = getIntent().getLongExtra("id", 0);
        if(id != 0){
            Thread t = new Thread(()->{
                salary = DatabaseAdapter.getInstance(this).salaryDao().fetchSalary(id);
                attendanceList = DatabaseAdapter.getInstance(this).attendanceDao().getAttendanceForMonth(salary.getMonth()+"%", salary.getEmpName());
            });
            t.start();
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
            salaryTxt.setText(AppUtil.getAmountWithSymbol(this, salary.getSalary()));
            restTxt.setText(String.valueOf(salary.getRestDays()));
            presentTxt.setText(String.valueOf(salary.getAttendance()));
            absentTxt.setText(String.valueOf(salary.getAbsent()));
            advanceTxt.setText(AppUtil.getAmountWithSymbol(this, salary.getAdvance()));
            penaltyTxt.setText(String.valueOf(salary.getTimePenalty()));
            diffTxt.setText(String.valueOf(salary.getDifference()));
            payDateTxt.setText(AppUtil.formatDateToVIew(salary.getPayDate()));
            toPayTxt.setText(AppUtil.getAmountWithSymbol(this, salary.getSalaryToPay()));
            paidTxt.setText(AppUtil.getAmountWithSymbol(this, salary.getAmountPaid()));
            String title = salary.getEmpName() + " - ";
            YearMonth y = YearMonth.parse(salary.getMonth(), DateTimeFormatter.ofPattern("yyyy/MM"));
            title = title.concat(y.format(DateTimeFormatter.ofPattern("MMM yy")));
            if(getSupportActionBar() != null)
                getSupportActionBar().setTitle(title);
            adapter.submitList(attendanceList);

        }
    }

    private void bottomDialog(){
        View bottomSheet = findViewById(R.id.home_bottom_sheet);
        BottomSheetBehavior mBottomSheetBehavior;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
    }
}
