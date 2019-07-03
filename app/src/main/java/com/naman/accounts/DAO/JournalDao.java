package com.naman.accounts.DAO;

import com.naman.accounts.Model.Journal;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface JournalDao {

    @Insert
    long insertTransaction(Journal transaction);

    @Update
    int updateTransaction(Journal transaction);

    @Delete
    int deleteTransaction(Journal transaction);

    @Query("delete from journal where id = :id")
    int deleteTransaction(long id);

    @Query("select * from `journal` where date= :date order by id ASC")
    List<Journal> fetchTransactionsForDate(String date);

    @Query("select * from journal where date =:date and accountName =:account order by id ASC")
    List<Journal> fetchTransactionsForAccountByDate(String account, String date);

    @Query("select * from journal where id = :id")
    Journal fetchTransactionById(long id);

    @Query("select * from journal where accountName =:account order by date ASC")
    List<Journal> fetchTransactionsForAccount(String account);

    @Query("select * from journal where date >= :startDate and date <= :endDate and accountName = :account")
    List<Journal> fetchBalanceForFy(String startDate, String endDate, String account);

    @Query("select * from journal where accountName =:account and date =(select min(date) from journal where accountName = :account)")
    Journal fetchFirstEntryForAccount(String account);

}