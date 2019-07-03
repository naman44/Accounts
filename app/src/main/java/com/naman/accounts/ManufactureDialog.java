package com.naman.accounts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Model.ItemJournal;
import com.naman.accounts.Model.ItemList;
import com.naman.accounts.Model.Manufacturing;
import com.naman.accounts.Model.Mapping;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.MappingListAdapter;
import com.naman.accounts.service.AppConstants;
import com.naman.accounts.service.AppUtil;
import com.naman.accounts.service.InventoryService;
import com.naman.accounts.service.RecyclerItemClickListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManufactureDialog extends Dialog {

    private Context context;
    private AutoCompleteTextView itemNameText;
    private EditText quantityText;
    private RecyclerView rv;
    private MappingListAdapter mapAdapter;
    private List<Mapping> mapList;
    private List<String> itemsList;
    Button saveBtn;
    private View selectedView;
    private Mapping selectedMap;

    public ManufactureDialog(Context c){
        super(c);
        this.context = c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_manufacture_new);

        itemNameText = findViewById(R.id.item_name_manufacture);
        quantityText = findViewById(R.id.quantity_manufacture);
        rv = findViewById(R.id.recycler_manufacture);
        saveBtn = findViewById(R.id.add_manufacture_btn);
        initRecycler();
        initAutoComplete();

       itemNameText.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)-> {
           String itemSelected = parent.getItemAtPosition(position).toString();
           Thread t = new Thread(()->{
               mapList = DatabaseAdapter.getInstance(context).mappingDao().fetchMappingListByItem(itemSelected);
           });
           t.start();
           try{
               t.join();
           }catch (Exception e){
               e.printStackTrace();
           }
           mapAdapter.updateList(mapList);
       });
       rv.addOnItemTouchListener(new RecyclerItemClickListener(context, (View v, int position)->{
           if(!v.isSelected()){
               if(selectedView != null)
                   selectedView.setSelected(false);
               v.setSelected(true);
               selectedView = v;
               selectedMap = mapList.get(position);
           }
           else {
               v.setSelected(false);
               selectedView = null;
               selectedMap = null;
           }
       }));
       saveBtn.setOnClickListener((View v)->{
           if(!itemNameText.getText().toString().isEmpty() && !quantityText.getText().toString().isEmpty()
                && selectedMap != null){
               Manufacturing m = new Manufacturing();
               m.setItemName(itemNameText.getText().toString());
               m.setMapId(selectedMap.getMapId());
               m.setQuantity(Double.parseDouble(quantityText.getText().toString()));
               new Thread(()->{
                   DatabaseAdapter db = DatabaseAdapter.getInstance(context);
                   db.manufacturingDao().insertManufacturing(m);
                   InventoryService service = new InventoryService(db);
                   service.updateInventoryForManufacturing(m);
                   long id = service.insertJournalEntry(createJournalEntry(m));
                   service.updateInventoryList(createItemList(id), AppConstants.JOURNAL_TYPE_MANUFACTURE);
               }).start();
               dismiss();
           }
       });
    }

    private ItemJournal createJournalEntry(Manufacturing m){
        ItemJournal journal = new ItemJournal();
        journal.setManufactureId(m.getId());
        journal.setDate(AppUtil.formatDate(LocalDate.now()));
        journal.setVendorName("Production");
        journal.setEntryType(AppConstants.JOURNAL_TYPE_MANUFACTURE);
        return journal;
    }

    private List<ItemList> createItemList(long id){
        List<ItemList> list = new ArrayList<>();
        list.add(createItemListObj(id, 1));
        if(selectedMap.getQuantity2() > 0)
            list.add(createItemListObj(id, 2));
        if(selectedMap.getQuantity3() > 0)
            list.add(createItemListObj(id, 3));
        return list;
    }

    private ItemList createItemListObj(long id, int count){
        ItemList item = new ItemList();
        if(count == 1){
            item.setJournalId(id);
            item.setQuantity(selectedMap.getQuantity1());
            item.setItemName(selectedMap.getItem1());
        }
        if(count == 2){
            item.setJournalId(id);
            item.setQuantity(selectedMap.getQuantity2());
            item.setItemName(selectedMap.getItem2());
        }
        if(count == 3){
            item.setJournalId(id);
            item.setQuantity(selectedMap.getQuantity3());
            item.setItemName(selectedMap.getItem3());
        }
        return item;
    }

    private void initAutoComplete(){
        Thread t = new Thread(()->{
           itemsList = DatabaseAdapter.getInstance(context).itemDao().fetchItemNames();
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, itemsList);
        itemNameText.setAdapter(adapter);
    }

    private void initRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(context);
        layoutRv.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(layoutRv);
        mapList = new ArrayList<>();
        mapAdapter = new MappingListAdapter(context, mapList);
        rv.setAdapter(mapAdapter);
        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
    }
}
