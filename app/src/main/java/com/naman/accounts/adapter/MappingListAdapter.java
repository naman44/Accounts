package com.naman.accounts.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naman.accounts.Model.Mapping;
import com.naman.accounts.R;

import java.util.List;

public class MappingListAdapter extends RecyclerView.Adapter<MappingListAdapter.MapViewHolder> {

    private Context context;
    private List<Mapping> mapList;
    private int selectedPosition = -1;
    private View selectedView;
    private SparseBooleanArray selectedItem = new SparseBooleanArray();

    public MappingListAdapter(Context context, List<Mapping> mapList){
        this.context = context;
        this.mapList = mapList;
    }

    class MapViewHolder extends RecyclerView.ViewHolder{

        private TextView mapName, component1, component2, component3, quantity1, quantity2, quantity3, itemName, itemQuantity;

        MapViewHolder(View v){
            super(v);
            mapName = v.findViewById(R.id.map_name_manufacture_dialog);
            component1 = v.findViewById(R.id.component1_name_dialog);
            component2 = v.findViewById(R.id.component2_name_dialog);
            component3 = v.findViewById(R.id.component3_name_dialog);
            quantity1 = v.findViewById(R.id.component1_quantity_dialog);
            quantity2 = v.findViewById(R.id.component2_quantity_dialog);
            quantity3 = v.findViewById(R.id.component3_quantity_dialog);
            itemName = v.findViewById(R.id.item_name_dialog);
            itemQuantity = v.findViewById(R.id.quantity_dialog);
        }

        public void setObj(Mapping map){
            mapName.setText(map.getMapName());
            component1.setText(map.getItem1());
            component2.setText(map.getItem2());
            component3.setText(map.getItem3());
            itemName.setText(map.getProductName());
            itemQuantity.setText(String.valueOf(map.getQuantityItem()));
            quantity1.setText(String.valueOf(map.getQuantity1()));
            if(map.getQuantity2() > 0)
                quantity2.setText(String.valueOf(map.getQuantity2()));
            if(map.getQuantity3() > 0){
                quantity3.setText(String.valueOf(map.getQuantity3()));
            }
        }
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_layout_horizontal_details, null);
        return new MapViewHolder(v);
    }

    @Override
    public int getItemCount() {
        if(mapList != null)
            return mapList.size();
        else
            return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewHolder holder, int position) {
        holder.setObj(mapList.get(position));
    }

    public void updateList(List<Mapping> mapNew){
        mapList.clear();
        mapList.addAll(mapNew);
        notifyDataSetChanged();
    }
}
