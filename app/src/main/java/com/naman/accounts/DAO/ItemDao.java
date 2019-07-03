package com.naman.accounts.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.naman.accounts.Model.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    public long insertItem(Item item);

    @Update
    public void updateItem(Item item);

    @Delete
    public void deleteItem(Item item);

    @Query("select * from item")
    List<Item> fetchItems();

    @Query("select * from item where itemName =:name")
    Item fetchItemByName(String name);

    @Query("select * from item")
    LiveData<List<Item>> fetchItemsLive();

    @Query("select itemName from item")
    List<String> fetchItemNames();

    @Query("select itemName from item where neck =:neck and itemType= 'Caps'")
    List<String> fetchCapsByNeck(double neck);
}
