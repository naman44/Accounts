package com.naman.accounts.Fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naman.accounts.ManufactureDialog;
import com.naman.accounts.screens.MappingScreen;
import com.naman.accounts.R;
import com.naman.accounts.screens.SalePurchaseScreen;

public class ManufacturingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_inventory, null);
        Button btn = view.findViewById(R.id.fragment_inventory_btn);
        Button btn2 = view.findViewById(R.id.fragment_inventory_view_btn);
        Button btn3 = view.findViewById(R.id.fragment_sale_purchase_btn);
        btn.setText("Mapping");
        btn2.setText("Manufacture");

        btn.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), MappingScreen.class));
        });

        btn2.setOnClickListener((View v)->{
            ManufactureDialog dialog = new ManufactureDialog(getActivity());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        });

        btn3.setOnClickListener((View v)->{
            startActivity(new Intent(getActivity(), SalePurchaseScreen.class));
        });
        return view;
    }
}
