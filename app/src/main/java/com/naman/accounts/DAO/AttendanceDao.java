package com.naman.accounts.DAO;

import com.naman.accounts.Model.Attendance;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AttendanceDao {

    @Insert
    void insertAttendance(Attendance attendance);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBatchAttendance(List<Attendance> attendances);

    @Update
    int updateAttendance(Attendance attendance);

    @Query("select * from attendance where date = :date")
    LiveData<List<Attendance>> fetchAttendanceForDateLive(String date);

    @Query("select * from attendance where date=:date and empName=:name")
    Attendance fetchAttendanceOfEmployeeByDate(String date, String name);

    @Query("select min(date) from attendance")
    String fetchFirstAttendanceDate();

    @Query("select e.empName, a.timeIn, a.timeOut, a.advance, a.date, a.present from employee e left join " +
            "attendance a on (a.empName = e.empName or a.empName is null) and a.date = :date where e.dateOfJoin <= :date and " +
            "(lastDate >= :date or lastDate = 'WORKING') and (a.date =:date or a.date is null)")
    List<Attendance> fetchAtt(String date);

    @Query("delete from attendance where date=:date")
    int deleteAttendanceForDate(String date);

    @Query("select * from attendance where date like :month and empName =:name")
    int countAttendance(String month, String name);

    // for display on salary page
    @Query("select date, timeIn, timeOut, advance, present, date as empName from attendance where date " +
            "like :month and empName =:name order by date desc")
    List<Attendance> getAttendanceForMonth(String month, String name);

    @Query("select sum(advance) from attendance where empName = :name and date >=" +
            "ifnull((select max(paydate) from salary where empName = :name and payDate < :firstDate)," +
            "(select min(date) from attendance where empName = :name))")
    double fetchAdvanceAmount(String name, String firstDate);



}
