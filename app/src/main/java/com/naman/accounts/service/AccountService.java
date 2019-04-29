package com.naman.accounts.service;

import com.naman.accounts.Model.Accounts;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.adapter.DatabaseAdapter;

public class AccountService {

    DatabaseAdapter db;

    public AccountService(DatabaseAdapter db){
        this.db = db;
    }

    public void updateAccountBalance(String accountName, double amount){
        Accounts acc = db.accountDao().fetchAccountByName(accountName);
        double update = acc.getClosingBalance() + amount;
        acc.setClosingBalance(update);
        db.accountDao().updateAccount(acc);
    }

    public void updateAccount(Journal j){
        if(j.getType() == AppUtil.INT_CREDIT){
            updateAccountBalance(j.getAccountName(), j.getAmount());
        }
        else
            updateAccountBalance(j.getAccountName(), j.getAmount()*-1);
    }
}
