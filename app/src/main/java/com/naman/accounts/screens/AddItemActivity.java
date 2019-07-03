package com.naman.accounts.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naman.accounts.Model.Item;
import com.naman.accounts.Model.ItemInventory;
import com.naman.accounts.Model.ItemListViewModel;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.adapter.ItemAdapter;
import com.naman.accounts.adapter.ListViewHolder;
import java.util.Arrays;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements ListViewHolder.ItemClickListener {

    Spinner itemType, itemPlastic;
    EditText textName, textColor, textWgt, textNeck, textSize, textUnit, textPacking, textQuantity;
    RecyclerView rv;
    String[] arrayType;
    ItemAdapter adapter;
    ItemListViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemType = findViewById(R.id.spinner_item_type_add);
        itemPlastic = findViewById(R.id.spinner_item_plastic_add);
        arrayType = getResources().getStringArray(R.array.item_type);
        textName = findViewById(R.id.input_item_name_add);
        textColor = findViewById(R.id.input_item_color_add);
        textWgt = findViewById(R.id.input_item_weight_add);
        textNeck = findViewById(R.id.input_item_neck_add);
        textPacking = findViewById(R.id.input_item_packing_add);
        textUnit = findViewById(R.id.input_item_unit_add);
        textSize = findViewById(R.id.input_item_size);
        textQuantity = findViewById(R.id.input_item_quantity_add);
        rv = findViewById(R.id.recycler_item_add);
        initializeRecycler();
        model = ViewModelProviders.of(this).get(ItemListViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab_item_add);
        fab.setOnClickListener((View v) -> {
            checkViewObject();
            if (textName.getError() == null) {
                Item item = createObjectFromView();
                model.upSertItem(item);
                resetView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.getItemsList().observe(this,
                (List<Item> items) -> adapter.submitList(items));
    }

    private void checkViewObject() {
        if (textName.getText() == null || textName.getText().toString().isEmpty()) {
            textName.setError("Mandatory");
        } else {
            textName.setError(null);
        }
    }

    private void initializeRecycler() {
        LinearLayoutManager layoutRv = new LinearLayoutManager(this);
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(layoutRv);
        adapter = new ItemAdapter(this);
        rv.setAdapter(adapter);
    }

    private Item createObjectFromView() {
        Item item = new Item();
        item.setItemName(textName.getText().toString());
        item.setColor(textColor.getText().toString());
        item.setUnit(textUnit.getText().toString());
        item.setItemType(itemType.getSelectedItem().toString());
        if (textNeck.getText() != null && !textNeck.getText().toString().isEmpty())
            item.setNeck(Double.parseDouble(textNeck.getText().toString()));
        if (textWgt.getText() != null && !textWgt.getText().toString().isEmpty())
            item.setWeight(Double.parseDouble(textWgt.getText().toString()));
        if (textPacking.getText() != null && !textPacking.getText().toString().isEmpty())
            item.setPacking(Integer.parseInt(textPacking.getText().toString()));
        if(textQuantity.getText() != null && !textQuantity.getText().toString().isEmpty())
            item.setQuantity(Double.parseDouble(textQuantity.getText().toString()));
        item.setSize(textSize.getText().toString());
        item.setPlastic(itemPlastic.getSelectedItem().toString());
        return item;
    }

    @Override
    public void onEditClicked(Object obj) {
        if(obj instanceof Item){
            textName.setText(((Item) obj).getItemName());
            textPacking.setText(String.valueOf(((Item) obj).getPacking()));
            textWgt.setText(String.valueOf(((Item) obj).getWeight()));
            textColor.setText(((Item) obj).getColor());
            textNeck.setText(String.valueOf(((Item) obj).getNeck()));
            textUnit.setText(((Item) obj).getUnit());
            itemType.setSelection(Arrays.asList(arrayType).indexOf(((Item) obj).getItemType()));
            itemPlastic.setSelection(Arrays.asList(
                    getResources().getStringArray(R.array.item_plastic)).indexOf(((Item) obj).getPlastic()));
            textSize.setText(((Item) obj).getSize());
            textQuantity.setText(String.valueOf(((Item) obj).getQuantity()));
        }
    }

    @Override
    public void onDeleteClicked(Object obj) {
        if(obj instanceof Item){
            model.deleteItem((Item) obj);
        }
    }

    private void resetView(){
        textName.setText(null);
        textUnit.setText(null);
        textNeck.setText(null);
        textColor.setText(null);
        textWgt.setText(null);
        textPacking.setText(null);
        textSize.setText(null);
        textQuantity.setText(null);
        itemType.setSelection(0);
        itemPlastic.setSelection(0);
    }
}
