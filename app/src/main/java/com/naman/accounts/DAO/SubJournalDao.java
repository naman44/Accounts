package com.naman.accounts.DAO;

import com.naman.accounts.Model.SubTransaction;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SubJournalDao {

    @Insert
    public void insertSub(SubTransaction sbt);

    @Query("select * from subtransaction where journalId = :id")
    public List<SubTransaction> fetchSubForTransaction(long id);
}
