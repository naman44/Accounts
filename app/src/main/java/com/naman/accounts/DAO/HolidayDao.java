package com.naman.accounts.DAO;

import com.naman.accounts.Model.Holidays;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HolidayDao {

    @Insert
    void insertHoliday(Holidays holidays);

    @Update
    void update(Holidays holidays);

    @Delete
    void delete(Holidays holidays);

    @Query("select * from holidays")
    List<Holidays> fetchHolidays();

    @Query("select count(*) from holidays where date=:date")
    int fetchHolidayByDate(String date);

    @Query("select count(*) from holidays where month=:month")
    int fetchHolidayForMonth(String month);

    @Query("select * from holidays where month =:month")
    List<Holidays> fetchHolidayListByMonth(String month);

    @Query("select distinct month from holidays")
    List<String> fetchHolidayMonths();

}
