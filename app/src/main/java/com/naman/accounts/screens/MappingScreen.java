package com.naman.accounts.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naman.accounts.Model.Mapping;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.util.List;

public class MappingScreen extends AppCompatActivity {

    private TextView componentName1, componentName2, componentName3, quantity1, quantity2, quantity3;
    private AutoCompleteTextView itemNameText, componentNameText;
    private EditText mapName, itemQuantity, componentQuantity;
    private ImageButton addComponentBtn;
    FloatingActionButton addMapBtn;
    private int componentCount;
    private List<String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_screen);

        initViews();
        initAutoComplete();

        addComponentBtn.setOnClickListener((View v)->{
            if(!componentNameText.getText().toString().isEmpty() && !componentQuantity.getText().toString().isEmpty()){
                if(componentCount == 0){
                    componentName1.setText(componentNameText.getText());
                    componentName1.setVisibility(View.VISIBLE);
                    quantity1.setText(componentQuantity.getText());
                    quantity1.setVisibility(View.VISIBLE);
                }
                else if(componentCount == 1){
                    componentName2.setText(componentNameText.getText());
                    componentName2.setVisibility(View.VISIBLE);
                    quantity2.setText(componentQuantity.getText());
                    quantity2.setVisibility(View.VISIBLE);
                }
                else if(componentCount == 2){
                    componentName3.setText(componentNameText.getText());
                    componentName3.setVisibility(View.VISIBLE);
                    quantity3.setText(componentQuantity.getText());
                    quantity3.setVisibility(View.VISIBLE);
                }
                componentCount++;
                componentNameText.setText("");
                componentQuantity.setText("");
            }
        });

        addMapBtn.setOnClickListener((View v)->{
            if(!mapName.getText().toString().isEmpty() && !itemNameText.getText().toString().isEmpty()
                    && !itemQuantity.getText().toString().isEmpty() && componentCount > 0){
                Mapping map = new Mapping();
                map.setMapName(mapName.getText().toString());
                map.setProductName(itemNameText.getText().toString());
                map.setQuantityItem(Double.parseDouble(itemQuantity.getText().toString()));
                map.setItem1(componentName1.getText().toString());
                map.setQuantity1(Double.parseDouble(quantity1.getText().toString()));
                if(!quantity2.getText().toString().isEmpty()){
                    map.setItem2(componentName2.getText().toString());
                    map.setQuantity2(Double.parseDouble(quantity2.getText().toString()));
                }
                if(!quantity3.getText().toString().isEmpty()){
                    map.setItem3(componentName3.getText().toString());
                    map.setQuantity3(Double.parseDouble(quantity3.getText().toString()));
                }
                new Thread(()->
                    DatabaseAdapter.getInstance(this).mappingDao().insertMap(map)).start();
                finish();
            }
        });
    }

    private void initViews(){
        componentName1 = findViewById(R.id.component_1_name);
        componentName2 = findViewById(R.id.component_2_name);
        componentName3 = findViewById(R.id.component_3_name);
        quantity1 = findViewById(R.id.component_1_quantity);
        quantity2 = findViewById(R.id.component_2_quantity);
        quantity3 = findViewById(R.id.component_3_quantity);
        itemNameText = findViewById(R.id.item_name_mapping);
        componentNameText = findViewById(R.id.component_name_input_mapping);
        componentQuantity = findViewById(R.id.quantity_component_mapping);
        mapName = findViewById(R.id.map_name_mapping);
        itemQuantity = findViewById(R.id.item_quantity_mapping);
        addComponentBtn = findViewById(R.id.add_component_mapping);
        addMapBtn = findViewById(R.id.fab_mapping);
        componentCount = 0;
    }

    private void initAutoComplete(){
        Thread t = new Thread(()->{
            itemList = DatabaseAdapter.getInstance(this).itemDao().fetchItemNames();
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, itemList);
        itemNameText.setAdapter(adapter);
        componentNameText.setAdapter(adapter);
    }
}
