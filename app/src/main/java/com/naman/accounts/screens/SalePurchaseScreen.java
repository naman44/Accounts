package com.naman.accounts.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naman.accounts.Model.Accounts;
import com.naman.accounts.Model.Item;
import com.naman.accounts.Model.ItemJournal;
import com.naman.accounts.Model.ItemList;
import com.naman.accounts.Model.Vendor;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.HorizontalStringAdapter;
import com.naman.accounts.adapter.ItemListBottomAdapter;
import com.naman.accounts.service.AccountService;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.InventoryService;
import com.naman.accounts.service.RecyclerItemClickListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.naman.accounts.adapter.DatabaseAdapter.getInstance;

public class SalePurchaseScreen extends AppCompatActivity {

    AutoCompleteTextView vendorName, itemName;
    EditText invoiceNum, quantityText, priceText;
    TextView dateText, totalAmountSansGst, vendorNameText, secondaryItem, secondaryQuantity;
    TextView billAmountBottom, totalAmountBottom, cgstAmount, sgstAmount, igstAmount;
    TextView billAmountText, totalAmountText;
    Button addItemBtn;
    ImageButton closeActivityBtn, cancelSecondaryBtn, infoBottomBtn;
    ToggleButton toggleBtn;
    RecyclerView rv, itemsRv;
    List<String> itemNames, vendorNames, itemNamesAss;
    Item selectedItem;
    HorizontalStringAdapter adapterRv;
    ItemListBottomAdapter bottomAdapter;
    BottomSheetBehavior bottomSheetBehavior;
    NestedScrollView layout;
    List<ItemList> itemsList;
    ItemJournal journalEntry;
    CheckBox interStateCheck;
    LinearLayout layoutSecondaryItem, cgstLayout, sgstLayout, igstLayout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_purchase_screen);

        initViews();
        initAutoComplete();
        initRecycler();

        addItemBtn.setOnClickListener((View v)->{
            if((itemName.getText() != null && !itemName.getText().toString().isEmpty())
                && (quantityText.getText() != null && !quantityText.getText().toString().isEmpty())){
                if(itemsList == null || itemsList.isEmpty()){
                    createJournalObj();
                    itemsList = new ArrayList<>();
                    itemsList.add(createListItemObj());
                    if(layoutSecondaryItem.getVisibility() == View.VISIBLE)
                        itemsList.add(createSecondaryItemObj());
                    resetItemViews();
                    disableViews();
                }
                else{
                    itemsList.add(createListItemObj());
                    if(layoutSecondaryItem.getVisibility() == View.VISIBLE)
                        itemsList.add(createSecondaryItemObj());
                    resetItemViews();
                }
                updateBottomSheet(itemsList);
                bottomAdapter.updateList(itemsList);
            }
        });

        itemName.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)-> {
                Thread t = new Thread(()->{
                    selectedItem = getInstance(parent.getContext()).itemDao()
                            .fetchItemByName(parent.getItemAtPosition(position).toString());
                    if(selectedItem.getItemType().equalsIgnoreCase("Bottle")){
                        itemNamesAss = getInstance(parent.getContext()).itemDao().fetchCapsByNeck(selectedItem.getNeck());
                    }
                    else
                        itemNamesAss.clear();
                });
                t.start();
                try{
                    t.join();
                }catch (Exception e){
                    e.printStackTrace();
                }
                adapterRv.updateList(itemNamesAss);
                rv.setVisibility(View.VISIBLE);
                layoutSecondaryItem.setVisibility(View.GONE);
        });

        closeActivityBtn.setOnClickListener((View v)-> finish());

        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, (View v, int position)->{
            String itemSelected = itemNamesAss.get(position);
            secondaryItem.setText(itemSelected);
            secondaryQuantity.setText(quantityText.getText());
            rv.setVisibility(View.GONE);
            layoutSecondaryItem.setVisibility(View.VISIBLE);
        }));

        cancelSecondaryBtn.setOnClickListener((View v)->{
            secondaryItem.setText("");
            secondaryQuantity.setText("");
            layoutSecondaryItem.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        });

        quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(layoutSecondaryItem.getVisibility() == View.VISIBLE)
                    secondaryQuantity.setText(quantityText.getText());
            }
        });

        infoBottomBtn.setOnClickListener((View v)->{
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            else
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        fab.setOnClickListener((View v)->{
            if(journalEntry != null && itemsList != null && itemsList.size() > 0){
                journalEntry.setTotalAmount(Double.parseDouble(billAmountBottom.getText().toString()));
                journalEntry.setAmountWithGst(Double.parseDouble(totalAmountBottom.getText().toString()));
                if(igstLayout.getVisibility() == View.VISIBLE){
                    journalEntry.setIgstRate(18);
                    journalEntry.setIgstAmount(Double.parseDouble(igstAmount.getText().toString()));
                }
                else{
                    journalEntry.setCgstRate(9);
                    journalEntry.setCgstAmount(Double.parseDouble(cgstAmount.getText().toString()));
                    journalEntry.setSgstRate(9);
                    journalEntry.setSgstAmount(Double.parseDouble(sgstAmount.getText().toString()));
                }
                journalEntry.setBalanceAmount(journalEntry.getAmountWithGst());
            }
            Thread t = new Thread(()->{
                // insert vendor if not present
                InventoryService service = new InventoryService(DatabaseAdapter.getInstance(this));
                long id = service.insertJournalEntry(journalEntry);
                if(id > 0){
                   for(ItemList i : itemsList){
                       i.setJournalId(id);
                       DatabaseAdapter.getInstance(this).itemListDao().insertListEntry(i);

                   }
                   service.updateInventoryList(itemsList, journalEntry.getEntryType());
               }
            });
            t.start();
            finish();
        });
    }

    private void initViews(){
        vendorName = findViewById(R.id.vendor_name_sp_new);
        invoiceNum = findViewById(R.id.invoice_sp_new);
        dateText = findViewById(R.id.date_sp_new);
        dateText.setText(AppUtil.formatDate(LocalDate.now()));
        quantityText = findViewById(R.id.quantity_sp_new);
        itemName = findViewById(R.id.item_name_sp_new);
        priceText = findViewById(R.id.price_sp_new);
        addItemBtn = findViewById(R.id.add_item_sp_new);
        toggleBtn = findViewById(R.id.toggle_sp_new);
        vendorNameText = findViewById(R.id.vendor_name_view_sp_new);
        interStateCheck = findViewById(R.id.checkbox_interstate_sp_new);
        layoutSecondaryItem = findViewById(R.id.layout_secondary_item_sp_new);
        layoutSecondaryItem.setVisibility(View.GONE);
        rv = findViewById(R.id.rv_sp_new);
        itemsRv = findViewById(R.id.rv_sp_list_bottom);
        layout = findViewById(R.id.nested_bottom_bill);
        closeActivityBtn = findViewById(R.id.close_activity_btn_sp_new);
        cancelSecondaryBtn = findViewById(R.id.secondary_row_close_btn);
        secondaryItem = findViewById(R.id.secondary_item_name_sp_new);
        secondaryQuantity = findViewById(R.id.secondary_item_quantity_sp_new);
        fab = findViewById(R.id.fab_sp_new);

        // bottom sheet behaviour
        bottomSheetBehavior = BottomSheetBehavior.from(layout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(160);

        //bottom layout
        totalAmountSansGst = findViewById(R.id.bill_amount_sp_bottom);
        billAmountBottom = findViewById(R.id.bill_amount_second_sp_bottom);
        totalAmountBottom = findViewById(R.id.total_amount_sp_bottom);
        billAmountText = findViewById(R.id.bill_amount_text_sp_bottom);
        totalAmountText = findViewById(R.id.total_amount_text_sp_bottom);
        cgstAmount = findViewById(R.id.cgst_amount_sp_bottom);
        sgstAmount = findViewById(R.id.sgst_amount_sp_bottom);
        igstAmount = findViewById(R.id.igst_amount_sp_bottom);
        cgstLayout = findViewById(R.id.layout_cgst_sp_bottom);
        sgstLayout = findViewById(R.id.layout_sgst_sp_bottom);
        igstLayout = findViewById(R.id.layout_igst_sp_bottom);
        infoBottomBtn = findViewById(R.id.info_img_sp_bottom);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(ev.getAction() ==MotionEvent.ACTION_DOWN){
//            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private void disableViews(){
        vendorNameText.setText(vendorName.getText().toString());
        vendorName.setVisibility(View.INVISIBLE);
        toggleBtn.setEnabled(false);
    }

    private void createJournalObj(){
        journalEntry = new ItemJournal();
        journalEntry.setVendorName(vendorName.getText().toString());
        journalEntry.setDate(dateText.getText().toString());
        journalEntry.setEntryType(AppUtil.getJournalType(toggleBtn.getText().toString()));
        journalEntry.setInvoiceNumber(invoiceNum.getText().toString());
    }

    private ItemList createListItemObj(){
        ItemList listItem = new ItemList();
        listItem.setItemName(itemName.getText().toString());
        listItem.setQuantity(Double.parseDouble(quantityText.getText().toString()));
        if(priceText.getText() != null && !priceText.getText().toString().isEmpty()){
            listItem.setPrice(Double.parseDouble(priceText.getText().toString()));
            listItem.setAmount(listItem.getQuantity()*listItem.getPrice());
        }
        return listItem;
    }

    private ItemList createSecondaryItemObj(){
        ItemList listItem = new ItemList();
        listItem.setItemName(secondaryItem.getText().toString());
        listItem.setQuantity(Double.parseDouble(secondaryQuantity.getText().toString()));
        // setting price 0
        return listItem;
    }

    private void resetItemViews(){
        itemName.setText("");
        quantityText.setText("");
        priceText.setText("");
        rv.setVisibility(View.VISIBLE);
        layoutSecondaryItem.setVisibility(View.GONE);
        secondaryItem.setText("");
        secondaryQuantity.setText("");
        adapterRv.updateList(new ArrayList<>());
    }

    private void initRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(layoutRv);
        itemNamesAss = new ArrayList<>();
        adapterRv = new HorizontalStringAdapter(itemNamesAss);
        rv.setAdapter(adapterRv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        itemsRv.setLayoutManager(layoutManager);
        itemsList = new ArrayList<>();
        bottomAdapter = new ItemListBottomAdapter(itemsList, this);
        itemsRv.setAdapter(bottomAdapter);
    }

    private void initAutoComplete(){
        Thread t = new Thread(()->{
            itemNames = getInstance(this).itemDao().fetchItemNames();
            vendorNames = getInstance(this).vendorDao().fetchVendorNames();
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapterItem = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, itemNames);
        itemName.setAdapter(adapterItem);
        itemName.setThreshold(2);
        ArrayAdapter<String> adapterVendor = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, vendorNames);
        vendorName.setAdapter(adapterVendor);
        vendorName.setThreshold(2);
    }

    public void updateBottomSheet(List<ItemList> list){
        itemsList = list;
        double totalAmount = 0;
        for(ItemList i : list){
            totalAmount += i.getAmount();
        }
        totalAmountSansGst.setText(String.valueOf(totalAmount));
        double cgst = (totalAmount*9)/100;
        double sgst = (totalAmount*9)/100;
        double igst = (totalAmount*18)/100;
        double finalAmount = totalAmount + cgst +sgst;
        billAmountBottom.setText(totalAmountSansGst.getText());
        if(interStateCheck.isChecked()){
            igstLayout.setVisibility(View.VISIBLE);
            cgstLayout.setVisibility(View.GONE);
            sgstLayout.setVisibility(View.GONE);
            igstAmount.setText(String.valueOf(igst));
        }
        else{
            igstLayout.setVisibility(View.GONE);
            cgstLayout.setVisibility(View.VISIBLE);
            sgstLayout.setVisibility(View.VISIBLE);
            cgstAmount.setText(String.valueOf(cgst));
            sgstAmount.setText(String.valueOf(sgst));
        }
        DecimalFormat df = new DecimalFormat("#.00");
        totalAmountBottom.setText(df.format(finalAmount));

    }
}
