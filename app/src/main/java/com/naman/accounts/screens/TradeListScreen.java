package com.naman.accounts.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.naman.accounts.Model.ItemJournal;
import com.naman.accounts.Model.ItemList;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.Payments;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.ItemListBottomAdapter;
import com.naman.accounts.adapter.PaymentListAdapter;
import com.naman.accounts.adapter.TradeListAdapter;
import com.naman.accounts.service.AccountService;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.RecyclerItemClickListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TradeListScreen extends AppCompatActivity {

    RecyclerView tradeListRv, itemsRv, paymentsRv;
    TradeListAdapter adapterTrade;
    ItemListBottomAdapter adapterItems;
    List<ItemJournal> listJournal;
    List<ItemList> listItems;
    TextView vendorName, dateText, billAmount, totalAmount, cgstAmount, sgstAmount, igstAmount, balanceTxt;
    TextView paymentDate;
    EditText paymentAmount;
    CheckBox fullCheck;
    LinearLayout cgstLayout, sgstLayout, igstLayout;
    ViewSwitcher switcher;
    ImageButton closeBtn, savePaymentBtn;
    ImageButton addPaymentBtn;
    ItemJournal selectedJournal;
    Spinner paymentType;
    List<Payments> paymentList;
    PaymentListAdapter adapterPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_list);

        initViews();
        initRecycler();

        closeBtn.setOnClickListener((View v)->{
            switcher.showPrevious();
        });

        addPaymentBtn.setOnClickListener((View v)->{
            paymentDate.setVisibility(View.VISIBLE);
            paymentAmount.setVisibility(View.VISIBLE);
            //fullCheck.setVisibility(View.VISIBLE);
            savePaymentBtn.setVisibility(View.VISIBLE);
            paymentType.setVisibility(View.VISIBLE);
            paymentDate.setText(AppUtil.formatDate(LocalDate.now()));
            fullCheck.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked)-> {
                    if(isChecked)
                        paymentAmount.setText(balanceTxt.getText());
                    else
                        paymentAmount.setText(String.valueOf(0));
            });
            paymentDate.setOnClickListener((View vi)->
                generateDatePicker());
            savePaymentBtn.setOnClickListener((View vi)->{
                if(!paymentAmount.getText().toString().isEmpty()){
                    Payments payment = new Payments();
                    payment.setAmount(Double.parseDouble(paymentAmount.getText().toString()));
                    payment.setDate(paymentDate.getText().toString());
                    payment.setJournalId(selectedJournal.getId());
                    payment.setRemark(paymentType.getSelectedItem().toString());
                    paymentList.add(payment);
                    balanceTxt.setText(String.valueOf(fetchBalance()));
                    selectedJournal.setBalanceAmount(fetchBalance());
                    if(fetchBalance() <= 0)
                        addPaymentBtn.setVisibility(View.GONE);
                    else
                        addPaymentBtn.setVisibility(View.VISIBLE);

                    new Thread(()->{
                       DatabaseAdapter db = DatabaseAdapter.getInstance(this);
                       db.paymentsDao().insertPayment(payment);
                       db.itemJournalDao().updateEntry(selectedJournal);

                       // update account, if cash
                       if(payment.getRemark().equalsIgnoreCase("Cash")){
                           Journal j = new Journal();
                           j.setAccountName("Cash");
                           j.setAmount(payment.getAmount());
                           j.setDate(payment.getDate());
                           if(selectedJournal.getEntryType() == AppConstants.JOURNAL_TYPE_PURCHASE){
                               j.setType(AppConstants.INT_CREDIT);
                               j.setRemark("Payment to " + selectedJournal.getVendorName());
                           }
                           else{
                               j.setType(AppConstants.INT_DEBIT);
                               j.setRemark("Payment from" + selectedJournal.getVendorName());
                           }
                           AccountService serv = new AccountService(db);
                           serv.newJournalEntry(j);
                           serv.updateItemJournalForPayment(selectedJournal.getVendorName(),
                                   selectedJournal.getEntryType(), payment.getAmount());
                       }
                    }).start();
                    adapterPayments.submitList(paymentList);
                    resetView();
                }
            });
        });
    }

    private void resetView(){
        paymentDate.setVisibility(View.GONE);
        paymentAmount.setVisibility(View.GONE);
        paymentAmount.setText("");
        fullCheck.setVisibility(View.GONE);
        savePaymentBtn.setVisibility(View.GONE);
        paymentType.setVisibility(View.GONE);
    }

    private void initViews(){
        tradeListRv = findViewById(R.id.recycler_trade_list);
        itemsRv = findViewById(R.id.recycler_trade_details);
        vendorName = findViewById(R.id.vendor_name_trade_detail);
        dateText = findViewById(R.id.date_trade_detail);
        billAmount = findViewById(R.id.bill_amount_second_sp_bottom);
        totalAmount = findViewById(R.id.total_amount_sp_bottom);
        cgstAmount = findViewById(R.id.cgst_amount_sp_bottom);
        sgstAmount = findViewById(R.id.sgst_amount_sp_bottom);
        igstAmount = findViewById(R.id.igst_amount_sp_bottom);
        cgstLayout = findViewById(R.id.layout_cgst_sp_bottom);
        sgstLayout = findViewById(R.id.layout_sgst_sp_bottom);
        igstLayout = findViewById(R.id.layout_igst_sp_bottom);
        switcher = findViewById(R.id.switch_trade_list);
        closeBtn = findViewById(R.id.close_btn_trade_details);
        balanceTxt = findViewById(R.id.balance_trade_details);
        addPaymentBtn = findViewById(R.id.add_payment_btn_trade_details);
        paymentDate = findViewById(R.id.date_payment_trade_details);
        paymentAmount = findViewById(R.id.payment_amount_trade_details);
        fullCheck = findViewById(R.id.checkBox_payment_trade_details);
        savePaymentBtn = findViewById(R.id.payment_save_trade_details);
        paymentType = findViewById(R.id.spinner_payment_trade_details);
        paymentsRv = findViewById(R.id.payments_rv_trade_details);

        paymentDate.setVisibility(View.GONE);
        paymentAmount.setVisibility(View.GONE);
        fullCheck.setVisibility(View.GONE);
        savePaymentBtn.setVisibility(View.GONE);
        paymentType.setVisibility(View.GONE);
        paymentList = new ArrayList<>();

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        switcher.setInAnimation(in);
        switcher.setOutAnimation(out);
    }

    private void initRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        tradeListRv.setLayoutManager(layoutRv);

        LinearLayoutManager layoutRv1 = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        itemsRv.setLayoutManager(layoutRv1);
        adapterItems = new ItemListBottomAdapter(new ArrayList<>(), this);
        itemsRv.setAdapter(adapterItems);

        LinearLayoutManager layoutRv2 = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        paymentsRv.setLayoutManager(layoutRv2);
        adapterPayments = new PaymentListAdapter();
        paymentsRv.setAdapter(adapterPayments);

        Thread t = new Thread(()->
            listJournal = DatabaseAdapter.getInstance(this).itemJournalDao().fetchAllTrades());
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        adapterTrade = new TradeListAdapter(this, listJournal);
        tradeListRv.setAdapter(adapterTrade);
        tradeListRv.addOnItemTouchListener(new RecyclerItemClickListener(this,
                (View view, int position)->{
                    switcher.showNext();
                    selectedJournal = listJournal.get(position);
                    Thread t1 = new Thread(()->{
                        listItems = DatabaseAdapter.getInstance(this).itemListDao().fetchItemListById(selectedJournal.getId());
                        paymentList = DatabaseAdapter.getInstance(this).paymentsDao().fetchPaymentsById(selectedJournal.getId());
                    });
                    t1.start();
                    try{
                       t1.join();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    fillJournalDetails();
                    adapterItems.updateList(listItems);
                    adapterPayments.submitList(paymentList);
                }));
    }

    private void fillJournalDetails(){
        vendorName.setText(selectedJournal.getVendorName());
        dateText.setText(selectedJournal.getDate());
        billAmount.setText(String.valueOf(selectedJournal.getTotalAmount()));
        totalAmount.setText(String.valueOf(selectedJournal.getAmountWithGst()));
        if(selectedJournal.getCgstAmount() > 0){
            igstLayout.setVisibility(View.GONE);
            cgstAmount.setText(String.valueOf(selectedJournal.getCgstAmount()));
            sgstAmount.setText(String.valueOf(selectedJournal.getSgstAmount()));
        }
        else{
            cgstLayout.setVisibility(View.GONE);
            sgstLayout.setVisibility(View.GONE);
            igstAmount.setText(String.valueOf(selectedJournal.getIgstAmount()));
        }
        balanceTxt.setText(String.valueOf(fetchBalance()));
        if(fetchBalance() <= 0)
            addPaymentBtn.setVisibility(View.GONE);
    }

    private double fetchBalance(){
        if(paymentList.size() > 0){
            double totalPayment = 0;
            for(Payments p : paymentList){
                totalPayment += p.getAmount();
            }
            return selectedJournal.getAmountWithGst() - totalPayment;
        }
        else
            return selectedJournal.getBalanceAmount();
    }

    private void generateDatePicker(){
        LocalDate date = AppUtil.formatLocalDateFromString(paymentDate.getText().toString());
        new DatePickerDialog(
                this, (DatePicker view, int year, int month, int dayOfMonth) ->{
            paymentDate.setText(AppUtil.formatDate(LocalDate.of(year, month + 1, dayOfMonth)));
        }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).show();
    }
}
