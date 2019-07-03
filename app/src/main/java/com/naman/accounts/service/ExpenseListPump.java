package com.naman.accounts.service;

import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.adapter.DatabaseAdapter;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpenseListPump {

    public static LinkedHashMap<Journal, List<SubTransaction>> returnExpenseMap(DatabaseAdapter db, String date, String account){
        LinkedHashMap<Journal, List<SubTransaction>> expenseListMap;
        expenseListMap = new LinkedHashMap<>();
        List<Journal> listJournal;
        if(account.equalsIgnoreCase("All"))
            listJournal = db.journalDao().fetchTransactionsForDate(date);
        else
            listJournal = db.journalDao().fetchTransactionsForAccountByDate(account, date);
        for(Journal j : listJournal){
            List<SubTransaction> list = db.subJournalDao().fetchSubForTransaction(j.getId());
            expenseListMap.put(j, list);
        }
        return expenseListMap;
    }
}
