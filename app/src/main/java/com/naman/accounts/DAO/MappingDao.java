package com.naman.accounts.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naman.accounts.Model.Mapping;

import java.util.List;

@Dao
public interface MappingDao {

    @Insert
    void insertMap(Mapping mapping);

    @Query("select * from mapping where productName=:itemName")
    List<Mapping> fetchMappingListByItem(String itemName);
}
