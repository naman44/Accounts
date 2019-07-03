package com.naman.accounts.screens;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.ToggleButton;

import com.google.android.material.textfield.TextInputEditText;
import com.naman.accounts.Model.Accounts;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.SubJournalListAdapter;
import com.naman.accounts.service.AccountService;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.JournalService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseCreationActivity extends AppCompatActivity {

    CheckBox detailsCheckBox;
    LinearLayout layoutDetails;
    AutoCompleteTextView accountNameTxt, remarkText;
    ToggleButton transactionType;
    TextInputEditText amountTxt, nameTxtSub, amountTxtSub;
    TextView dateTxt;
    Journal fromTransactionObj;
    Journal toTransactionObj;
    ImageButton addDetailLineBtn;
    List<SubTransaction> listSub;
    RecyclerView subRv;
    List<String> accountNamesList;
    FloatingActionButton fab;
    SubJournalListAdapter adapter;
    Accounts toAccount;
    DatabaseAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews();
        initializeAutoComplete();
        initializeRecyclerView();

        fab.setOnClickListener((View view)-> {
                checkValues();
                if(amountTxt.getError() == null && accountNameTxt.getError() == null){
                    saveTransactionToDb();
                    finish();
                }
        });

        detailsCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked)-> {
            if(isChecked){
                toggleViewEdit(0);
            }
            if(accountNameTxt.getError() != null || amountTxt.getError() != null)
                detailsCheckBox.setChecked(false);
            if(!isChecked){
                toggleViewEdit(1);
            }
        });

        addDetailLineBtn.setOnClickListener((View v)->{
            double amountSub = 0;
            if(!nameTxtSub.getText().toString().isEmpty() && !amountTxtSub.getText().toString().isEmpty()){
                SubTransaction sub = new SubTransaction();
                sub.setAmount(Double.parseDouble(amountTxtSub.getText().toString()));
                sub.setName(nameTxtSub.getText().toString());
                amountSub = sub.getAmount();
                listSub.add(sub);
                nameTxtSub.setText("");
                amountTxtSub.setText("");
            }
            if(listSub.size() == 1){
                amountTxt.setText(String.valueOf(amountSub));
            }
            else{
                double amountPage = Double.parseDouble(amountTxt.getText().toString());
                amountTxt.setText(String.valueOf(amountPage + amountSub));
            }
            if(listSub.size() > 0){
                detailsCheckBox.setVisibility(View.GONE);
            }
            adapter.submitList(listSub);
            adapter.notifyDataSetChanged();
        });
    }

    private void initializeViews(){
        detailsCheckBox = findViewById(R.id.expense_creation_details_check);
        layoutDetails = findViewById(R.id.expense_creation_layout_sub);
        accountNameTxt = findViewById(R.id.expense_creation_account);
        transactionType = findViewById(R.id.expense_creation_toggle);
        amountTxt = findViewById(R.id.expense_creation_amount);
        remarkText = findViewById(R.id.expense_creation_remark);
        nameTxtSub = findViewById(R.id.expense_creation_sub_name);
        amountTxtSub = findViewById(R.id.expense_creation_sub_amount);
        dateTxt = findViewById(R.id.expense_creation_date);
        addDetailLineBtn = findViewById(R.id.expense_add_line_btn);
        subRv = findViewById(R.id.expense_creation_sub_recycler);
        fab = findViewById(R.id.fab);
        listSub = new ArrayList<>(1);
        dateTxt.setText(AppUtil.formatDate(LocalDate.now()));
        db = DatabaseAdapter.getInstance(this);

        if(getIntent().getStringExtra("date") != null){
            dateTxt.setText(getIntent().getStringExtra("date"));
        }
    }

    private void initializeRecyclerView(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        subRv.setLayoutManager(layoutRv);
        adapter = new SubJournalListAdapter();
        subRv.setAdapter(adapter);
    }

    private void initializeAutoComplete(){
        Thread t = new Thread(()->
           accountNamesList = db.accountDao().fetchAccountNames());
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, accountNamesList);
        accountNameTxt.setAdapter(adapter);
        remarkText.setAdapter(adapter);
        remarkText.setThreshold(2);
        accountNameTxt.setThreshold(2);

        remarkText.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)-> {
            String selectedAccount = parent.getItemAtPosition(position).toString();
            new Thread(()->
                toAccount = db.accountDao().fetchAccountByName(selectedAccount)).start();
        });
    }

    private void checkValues(){
        if(accountNameTxt.getText().toString().isEmpty())
            accountNameTxt.setError("Mandatory Field!");
        else
            accountNameTxt.setError(null);
        if(amountTxt.getText().toString().isEmpty())
            amountTxt.setError("Mandatory Field!");
        else
            amountTxt.setError(null);
    }

    private void saveTransactionToDb(){
            new Thread(()->{
                // insert account, if needed
                if(!accountNamesList.contains(accountNameTxt.getText().toString())){
                    db.accountDao()
                            .insertAccount(new Accounts(accountNameTxt.getText().toString(),
                                    AppConstants.AC_TYPE_EXPENSE));
                }
                JournalService service = new JournalService(db);
                // first entry with sublist
                if(amountTxt.getText() != null && !amountTxt.getText().toString().isEmpty()) {
                    service.createJournal(accountNameTxt.getText().toString(),
                            dateTxt.getText().toString(), transactionType.isChecked(), remarkText.getText().toString(),
                            Double.parseDouble(amountTxt.getText().toString()), listSub);
                    // second entry for double entry system, no sublist allowed
                    if (toAccount != null) {
                        service.createJournal(toAccount.getName(), dateTxt.getText().toString(),
                                !transactionType.isChecked(), generateRemark(),
                                Double.parseDouble(amountTxt.getText().toString()), null);
                    }
                }
            }).start();
        }

        private String generateRemark(){
        String remark = "Transfer";
            if(toAccount.getType() == AppConstants.AC_TYPE_SALARY){
                remark = "Advance Paid";
            }
            else if(toAccount.getType() == AppConstants.AC_TYPE_VENDOR){
                if(transactionType.isChecked())
                    remark = "Payment received";
                else
                    remark = "Payment Done";
            }
            return remark;
        }

    private void toggleViewEdit(int value){
        if(value == 0){
            layoutDetails.setVisibility(View.VISIBLE);
            amountTxt.setInputType(InputType.TYPE_NULL);
            transactionType.setEnabled(false);
        }
        else{
            layoutDetails.setVisibility(View.GONE);
            amountTxt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            transactionType.setEnabled(true);
        }
    }
}
