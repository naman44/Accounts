package com.naman.accounts.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naman.accounts.Model.Vendor;

import java.util.List;

@Dao
public interface VendorDao {

    @Insert
    public void insertVendor(Vendor vendor);

    @Query("select vendorName from vendor")
    List<String> fetchVendorNames();

    @Query("select count(*) from vendor where vendorName =:name")
    int checkVendorName(String name);
}
