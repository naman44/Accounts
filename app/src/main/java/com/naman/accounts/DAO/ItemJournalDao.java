package com.naman.accounts.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.naman.accounts.Model.ItemJournal;

import java.util.List;

@Dao
public interface ItemJournalDao {

    @Insert
    public long insertEntry(ItemJournal itemJournal);

    @Update
    public void updateEntry(ItemJournal itemJournal);

    @Query("select * from itemjournal where entrytype= 0")
    List<ItemJournal> fetchAllTrades();
}
