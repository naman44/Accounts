package com.naman.accounts.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naman.accounts.Model.Payments;

import java.util.List;

@Dao
public interface PaymentsDao {

    @Insert
    public void insertPayment(Payments payments);

    @Query("select * from payments where journalId =:id")
    List<Payments> fetchPaymentsById(long id);
}
