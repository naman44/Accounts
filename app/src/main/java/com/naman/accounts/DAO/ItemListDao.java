package com.naman.accounts.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naman.accounts.Model.ItemList;

import java.util.List;

@Dao
public interface ItemListDao {

    @Insert
    void insertListEntry(ItemList itemList);

    @Query("select * from itemlist where journalId=:id")
    List<ItemList> fetchItemListById(long id);
}
