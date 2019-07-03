package com.naman.accounts.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.naman.accounts.Model.Manufacturing;

@Dao
public interface ManufacturingDao {

    @Insert
    void insertManufacturing(Manufacturing manufacturing);
}
