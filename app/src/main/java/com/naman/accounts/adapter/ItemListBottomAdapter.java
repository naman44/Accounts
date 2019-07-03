package com.naman.accounts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Model.ItemList;
import com.naman.accounts.R;
import com.naman.accounts.screens.SalePurchaseScreen;
import com.naman.accounts.screens.TradeListScreen;

import java.util.List;

public class ItemListBottomAdapter extends RecyclerView.Adapter<ItemListBottomAdapter.ItemsViewHolder> {

    private List<ItemList> itemsList;
    private Context context;

    public class ItemsViewHolder extends RecyclerView.ViewHolder{

        TextView itemName, itemQuantityPrice, totalAmount;
        ImageButton deleteBtn;

        ItemsViewHolder(View view){
            super(view);
            itemName = view.findViewById(R.id.bottom_sp_item_name);
            itemQuantityPrice = view.findViewById(R.id.bottom_sp_item_quantity_price);
            totalAmount = view.findViewById(R.id.bottom_sp_total_amount);
            deleteBtn = view.findViewById(R.id.bottom_sp_delete_btn);
        }

        public void setObj(ItemList item){
            itemName.setText(item.getItemName());
            itemQuantityPrice.setText(String.valueOf(item.getQuantity()) + " @ " + String.valueOf(item.getPrice()));
            totalAmount.setText(item.getQuantity()*item.getPrice() + "");
            if(context instanceof TradeListScreen){
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener((View v)->{
                itemsList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                ((SalePurchaseScreen)context).updateBottomSheet(itemsList);
            });
        }
    }

    public ItemListBottomAdapter(List<ItemList> itemsList, Context context){
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sale_purchase, null);
        return new ItemsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.setObj(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void updateList(List<ItemList> newList){
        itemsList.clear();
        itemsList.addAll(newList);
    }
}
