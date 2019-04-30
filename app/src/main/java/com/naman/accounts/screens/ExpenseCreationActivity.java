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
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.TransactionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseCreationActivity extends AppCompatActivity {

    CheckBox detailsCheckBox;
    LinearLayout layoutDetails;
    AutoCompleteTextView accountNameTxt;
    ToggleButton transactionType;
    TextInputEditText amountTxt, remarkTxt, nameTxtSub, amountTxtSub;
    TextView dateTxt;
    Journal transactionObj;
    ImageButton addDetailLineBtn;
    List<SubTransaction> listSub;
    RecyclerView subRv;
    List<String> accountNamesList;
    FloatingActionButton fab;
    SubJournalListAdapter adapter;
    boolean isNew;
    Journal oldJournal;


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
        remarkTxt = findViewById(R.id.expense_creation_remark);
        nameTxtSub = findViewById(R.id.expense_creation_sub_name);
        amountTxtSub = findViewById(R.id.expense_creation_sub_amount);
        dateTxt = findViewById(R.id.expense_creation_date);
        addDetailLineBtn = findViewById(R.id.expense_add_line_btn);
        subRv = findViewById(R.id.expense_creation_sub_recycler);
        fab = findViewById(R.id.fab);
        transactionObj = new Journal();
        listSub = new ArrayList<>(1);

        isNew = true;
        String date = getIntent().getStringExtra("date");
        long id = getIntent().getLongExtra("id", 0);
        if (date != null){
            dateTxt.setText(date);
        }
        else if(id != 0){
            isNew = false;
            fillViewForEdit(id);
        }
        else
            dateTxt.setText(AppUtil.formatDate(LocalDate.now()));
    }

    private void fillViewForEdit(long id){
        Thread t = new Thread(()->{
            oldJournal = DatabaseAdapter.getInstance(this).journalDao().fetchTransactionById(id);
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(oldJournal != null){
            accountNameTxt.setText(oldJournal.getAccountName());
            remarkTxt.setText(oldJournal.getRemark());
            amountTxt.setText(String.valueOf(oldJournal.getAmount()));
            if(oldJournal.getType() == AppUtil.INT_CREDIT){
                transactionType.setChecked(false);
            }
            else
                transactionType.setChecked(true);
            dateTxt.setText(oldJournal.getDate());
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
        Thread t = new Thread(()->{
           accountNamesList = DatabaseAdapter.getInstance(this).accountDao().fetchAccountNames();
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, accountNamesList);
        accountNameTxt.setAdapter(adapter);
        accountNameTxt.setThreshold(2);
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
            setTransactionObject();
            Thread t = new Thread(()->{
                DatabaseAdapter db = DatabaseAdapter.getInstance(this);
                insertAccount(db);
                long id = 0;
                if(oldJournal != null)
                    id = oldJournal.getId();
                if(isNew){
                    id = db.journalDao().insertTransaction(transactionObj);
                }
                else{
                    transactionObj.setId(id);
                    db.journalDao().updateTransaction(transactionObj);
                }
                if(id != 0){
                    transactionObj.setId(id);
                    insertSub(db);
                    updateAccount(db);
                }
            });
            t.start();
            try{
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
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

    private void setTransactionObject(){
        if(accountNameTxt.getError() == null && amountTxt.getError() == null) {
            transactionObj.setAccountName(accountNameTxt.getText().toString());
            transactionObj.setDate(dateTxt.getText().toString());
            transactionObj.setRemark(remarkTxt.getText().toString());
            transactionObj.setAmount(Double.parseDouble(amountTxt.getText().toString()));
            if (transactionType.isChecked()) {
                transactionObj.setType(AppUtil.INT_DEBIT);
            } else
                transactionObj.setType(AppUtil.INT_CREDIT);

        }
    }

    private void insertAccount(DatabaseAdapter db){
        if(!accountNamesList.contains(accountNameTxt.getText().toString())){
            db.accountDao()
                    .insertAccount(new Accounts(accountNameTxt.getText().toString(), AppUtil.AC_TYPE_EXPENSE));
        }
    }

    private void updateAccount(DatabaseAdapter db){
        if(isNew){
            if(transactionObj.getType() == AppUtil.INT_DEBIT){
                new AccountService(db)
                        .updateAccountBalance(transactionObj.getAccountName(), transactionObj.getAmount());
            }
            else{
                new AccountService(db)
                        .updateAccountBalance(transactionObj.getAccountName(), transactionObj.getAmount()*-1);
            }
        }
        else{
            if(transactionObj.getType() == AppUtil.INT_CREDIT && oldJournal.getType() == AppUtil.INT_CREDIT){
                double amount = transactionObj.getAmount() - oldJournal.getAmount();
                new AccountService(db)
                        .updateAccountBalance(transactionObj.getAccountName(), amount*-1);
            }
            else if(transactionObj.getType() == AppUtil.INT_DEBIT && oldJournal.getType() == AppUtil.INT_DEBIT){
                double amount = transactionObj.getAmount() - oldJournal.getAmount();
                new AccountService(db)
                        .updateAccountBalance(transactionObj.getAccountName(), amount);
            }
            else if(transactionObj.getType() == AppUtil.INT_CREDIT && oldJournal.getType() == AppUtil.INT_DEBIT){
                double amount = transactionObj.getAmount() + oldJournal.getAmount();
                new AccountService(db)
                        .updateAccountBalance(transactionObj.getAccountName(), amount*-1);
            }
            else{
                double amount = transactionObj.getAmount() + oldJournal.getAmount();
                new AccountService(db)
                        .updateAccountBalance(transactionObj.getAccountName(), amount);
            }
        }
    }

    private void insertSub(DatabaseAdapter db){
        if(listSub != null && listSub.size() > 0){
            for(SubTransaction tr : listSub){
                tr.setJournalId(transactionObj.getId());
                db.subJournalDao().insertSub(tr);
            }
        }
    }

}
