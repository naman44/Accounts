package com.naman.accounts.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.naman.accounts.Model.Item;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.InventoryListAdapter;

import java.util.List;

public class InventoryScreen extends AppCompatActivity {

    RecyclerView rv;
    InventoryListAdapter adapter;
    List<Item> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_screen);

        rv = findViewById(R.id.recycler_inventory_display);
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(layoutRv);
        adapter = new InventoryListAdapter();
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread t = new Thread(()->{
            listItems = DatabaseAdapter.getInstance(this).itemDao().fetchItems();
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        adapter.submitList(listItems);
    }
}
