package com.naman.accounts.BottomFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.naman.accounts.ExpenseActivity;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.ExpenseCreationActivity;
import com.naman.accounts.service.AccountService;
import com.naman.accounts.service.AppUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomExpenseFragment extends BottomSheetDialogFragment{

    LinearLayout layoutEdit, layoutDelete;
    List<SubTransaction> list;
    long id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.bottom_option_expense, container, false);
        layoutEdit = v.findViewById(R.id.linear_edit_expense_bottom);
        layoutDelete = v.findViewById(R.id.linear_delete_expense_bottom);

        if(getArguments() != null){
            id = getArguments().getLong("id");
            if(id != 0){
                Thread t = new Thread(()->{
                    list = DatabaseAdapter.getInstance(getActivity()).subJournalDao().fetchSubForTransaction(id);
                });
                t.start();
                try{
                    t.join();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        if(list != null && !list.isEmpty()){
            layoutEdit.setVisibility(View.GONE);
        }
        else
            layoutEdit.setVisibility(View.VISIBLE);

        layoutEdit.setOnClickListener((View vi)->{
            Intent intent = new Intent(getActivity(), ExpenseCreationActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        layoutDelete.setOnClickListener((View vi)->{
            new Thread(()->{
                DatabaseAdapter db = DatabaseAdapter.getInstance(getActivity());
                Journal j = db.journalDao().fetchTransactionById(id);
                int x = db.journalDao().deleteTransaction(id);
                if(x > 0){
                    new AccountService(db).updateAccount(j);
                }
            }).start();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getActivity() instanceof ExpenseActivity){
            ExpenseActivity act = (ExpenseActivity) getActivity();
            act.fillRecycler();
        }
    }
}
