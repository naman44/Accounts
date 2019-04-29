package com.naman.accounts.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.naman.accounts.ExpenseActivity;
import com.naman.accounts.Model.Accounts;
import com.naman.accounts.R;
import com.naman.accounts.adapter.AccountsListAdapter;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExpensesFragment extends Fragment {

    RecyclerView accountsRv;
    AccountsListAdapter adapter;
    List<Accounts> accountList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_expense_fragment_main, null);
        Button expenseBtn = view.findViewById(R.id.add_expense_btn);
        accountsRv = view.findViewById(R.id.recycler_expense_main_accounts_list);

        initializeRecycler();

        expenseBtn.setOnClickListener((View v)->
            startActivity(new Intent(getActivity(), ExpenseActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillRecycler();
    }

    private void initializeRecycler(){
        LinearLayoutManager layoutRv = new LinearLayoutManager(getContext());
        layoutRv.setOrientation(RecyclerView.VERTICAL);
        accountsRv.setLayoutManager(layoutRv);
        adapter = new AccountsListAdapter();
        accountsRv.setAdapter(adapter);
    }

    private void fillRecycler(){
        Thread t = new Thread(()->{
            accountList = DatabaseAdapter.getInstance(getContext()).accountDao().fetchAllAccounts();
        });
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        adapter.submitList(accountList);
    }
}
