package com.naman.accounts.DAO;

import com.naman.accounts.Model.Salary;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SalaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSalary(Salary salary);

    @Update
    int updateSalary(Salary salary);

    @Query("select * from salary where id=:id")
    Salary fetchSalary(long id);

    @Query("select * from salary where empName = :empName and month = :month")
    Salary fetchSalary(String month, String empName);

    @Query("select min(month) from salary where payDate is null or payDate = ''")
    String getMonthUnpaid();

    @Query("select max(month) from salary where payDate is not null")
    String getMonthPaid();

    @Query("select s.id, s.empName, e.empSalary as salary, s.salaryToPay, s.advance, s.restDays, s.payDate, s.month, s.attendance, s.absent, s.amountPaid, s.difference, s.timePenalty " +
            "from salary s join employee e on e.empName = s.empName where month =:month")
    List<Salary> fetchSalaryForMonth(String month);

    @Query("select max(s.paydate) from attendance a join salary s on a.empName = s.empName " +
            "where a.empName =:empName and s.payDate < :date")
    String fetchLastPayDate(String empName, String date);

    @Query("select s.id, e.empName, e.empSalary as salary, s.salaryToPay, s.advance, s.restDays, s.payDate, s.month, s.attendance, s.absent, s.amountPaid, s.difference, s.timePenalty " +
            "from employee e left join " +
            "salary s on (s.empName = e.empName or s.empName is null) and s.month = :month ")
    List<Salary> getUnpaidSalary(String month);



}
