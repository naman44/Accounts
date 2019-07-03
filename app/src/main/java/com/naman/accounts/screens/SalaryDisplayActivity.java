package com.naman.accounts.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.naman.accounts.BottomFragments.BottomSalaryFragment;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.SalaryListAdapter;
import com.naman.accounts.adapter.SalaryListHolder;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.OnSwipeTouchListener;
import com.naman.accounts.service.RecyclerItemClickListener;
import com.naman.accounts.service.SalaryService;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SalaryDisplayActivity extends AppCompatActivity implements SalaryListHolder.mySalaryTouchListener {

    TextView monthName, totalSalary;
    RelativeLayout layoutPage;
    RecyclerView salaryRv;
    SalaryListAdapter adapter;
    List<Salary> list;
    public String monthValue;
    DatabaseAdapter db;
    ImageButton refreshBtn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_display);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        monthName = findViewById(R.id.month_salary_page);
        layoutPage = findViewById(R.id.layout_salary_page);
        totalSalary = findViewById(R.id.total_salary_page);
        salaryRv = findViewById(R.id.recycler_salary_list);
        refreshBtn = findViewById(R.id.refresh_salary_btn);

        salaryRv.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
            }
            @Override
            public void onSwipeLeft() {
                monthValue = AppUtil.adjustMonth(monthValue, "yyyy/MM", 1);
                getDisplayList(monthValue);
            }
            @Override
            public void onSwipeRight() {
                monthValue = AppUtil.adjustMonth(monthValue, "yyyy/MM", -1);
                getDisplayList(monthValue);
            }
            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
            }
        });
        refreshBtn.setOnClickListener((View v)->{
            Thread t = new Thread(()->{
               list = new SalaryService(DatabaseAdapter.getInstance(this)).updateSalaryBatch(monthValue);
            });
            t.start();
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
            getDisplayList(monthValue);
        });

        initiateRecycler();
        bottomDialog();
    }
    private void initiateRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        salaryRv.setLayoutManager(layoutRv);
        adapter = new SalaryListAdapter(this);
        salaryRv.setAdapter(adapter);

//        salaryRv.addOnItemTouchListener(new RecyclerItemClickListener(this,
//                (View view, int position)->{
//                    Intent intent = new Intent(this, SalaryDetailActivity.class);
//                    intent.putExtra("id", list.get(position).getId());
//                    startActivity(intent);
//        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDisplayList("");
    }

    public void getDisplayList(String month){
        db = DatabaseAdapter.getInstance(this);
        Thread t = new Thread(()->{
            if(month.isEmpty()){
                monthValue = new SalaryService(db).getMonthForResume();
            }
            list = new SalaryService(db).generateSalaryByMonth(monthValue);
        });
        t.start();
        try{
            t.join();
            adapter.submitList(list);
            YearMonth y = YearMonth.parse(monthValue, DateTimeFormatter.ofPattern("yyyy/MM"));
            monthName.setText(y.format(DateTimeFormatter.ofPattern("MMM yy")));
        }catch (Exception e){
            e.printStackTrace();
        }
        displayTotalSalary();
    }

    private void displayTotalSalary(){
        double total = 0;
        for(Salary sd : list){
            if(sd.getPayDate() == null || sd.getPayDate().isEmpty())
                total += sd.getSalaryToPay();
        }
        totalSalary.setText(AppUtil.getAmountWithSymbol(this, total));
    }

    @Override
    public void onPayClicked(long id) {
        BottomSalaryFragment frag = new BottomSalaryFragment();
        Bundle b = new Bundle();
        b.putLong("id", id);
        frag.setArguments(b);
        frag.show(getSupportFragmentManager(), frag.getTag());
    }

    @Override
    public void onDetailClicked(long id) {
        Intent intent = new Intent(this, SalaryDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void bottomDialog(){
        View bottomSheet = findViewById(R.id.salary_bottom_sheet);
        BottomSheetBehavior mBottomSheetBehavior;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
    }
}
