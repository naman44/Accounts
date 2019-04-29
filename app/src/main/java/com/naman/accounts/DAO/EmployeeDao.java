package com.naman.accounts.DAO;

import com.naman.accounts.Model.Employee;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertEmployee(Employee employee);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int updateEmployee(Employee employee);

    @Query("select * from employee")
    LiveData<List<Employee>> fetchAllEmployees();

    @Query("select empName from employee")
    List<String> fetchEmployeeNames();

    @Query("select empName from employee where dateOfJoin <= :date and (lastDate = 'WORKING' or lastDate >= :date) ")
    List<String> fetchEmployeesForAttendance(String date);

    @Query("delete from employee where empName =:name")
    int deleteEmployee(String name);
}
