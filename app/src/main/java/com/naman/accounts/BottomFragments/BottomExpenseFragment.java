package com.naman.accounts.BottomFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.naman.accounts.screens.ExpenseActivity;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.R;
import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.screens.ExpenseCreationActivity;
import com.naman.accounts.service.AccountService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomExpenseFragment extends BottomSheetDialogFragment{

    LinearLayout layoutDelete;
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
        layoutDelete.setOnClickListener((View vi)->{
            new Thread(()->{
                DatabaseAdapter db = DatabaseAdapter.getInstance(getActivity());
                Journal j = db.journalDao().fetchTransactionById(id);
                new AccountService(db).deleteJournalEntry(j);
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
