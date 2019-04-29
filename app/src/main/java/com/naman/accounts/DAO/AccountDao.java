package com.naman.accounts.DAO;

import com.naman.accounts.Model.Accounts;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AccountDao {

    @Insert
    void insertAccount(Accounts account);

    @Update
    int updateAccount(Accounts account);

    @Delete
    int deleteAccount(Accounts account);

    @Query("select name from accounts")
    List<String> fetchAccountNames();

    @Query("select * from accounts where name= :name")
    Accounts fetchAccountByName(String name);

    @Query("select * from accounts")
    List<Accounts> fetchAllAccounts();
}
