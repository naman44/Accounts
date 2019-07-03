package com.naman.accounts.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naman.accounts.screens.InventoryScreen;
import com.naman.accounts.R;
import com.naman.accounts.screens.SalePurchaseScreen;
import com.naman.accounts.screens.AddItemActivity;
import com.naman.accounts.screens.TradeListScreen;

public class InventoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout_inventory, null);
        Button btn = view.findViewById(R.id.fragment_inventory_btn);
        Button btn2 = view.findViewById(R.id.fragment_inventory_view_btn);
        Button btn3 = view.findViewById(R.id.fragment_sale_purchase_btn);
        Button btn4 = view.findViewById(R.id.fragment_trade_list_btn);

        btn.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), AddItemActivity.class));
        });

        btn2.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), InventoryScreen.class));
        });

        btn3.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), SalePurchaseScreen.class));
        });

        btn4.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), TradeListScreen.class));
        });
        return view;
    }
}
