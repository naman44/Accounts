package com.naman.accounts.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naman.accounts.Model.AccountBalance;

@Dao
public interface AccountBalanceDao {

    @Insert
    void insertBalance(AccountBalance balance);

    @Query("select openingBalance from accountbalance where period =:date and accountName =:accountName")
    double fetchOpeningByDate(String date, String accountName);

    @Query("select count(*) from accountbalance where period =:date and accountName =:accountName")
    int checkAccountBalanceEntry(String date, String accountName);
}
