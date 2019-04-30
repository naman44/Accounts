package com.naman.accounts.service;

import com.naman.accounts.Model.Journal;
import com.naman.accounts.adapter.DatabaseAdapter;

public class TransactionService {

    private DatabaseAdapter db;

    public TransactionService(DatabaseAdapter db){
        this.db = db;
    }

    public Journal fetchJournalById(long id){
        return db.journalDao().fetchTransactionById(id);
    }


}
