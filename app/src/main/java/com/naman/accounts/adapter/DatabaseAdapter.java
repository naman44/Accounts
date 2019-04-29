package com.naman.accounts.adapter;

import android.content.Context;

import com.naman.accounts.DAO.AccountDao;
import com.naman.accounts.DAO.AttendanceDao;
import com.naman.accounts.DAO.EmployeeDao;
import com.naman.accounts.DAO.HolidayDao;
import com.naman.accounts.DAO.JournalDao;
import com.naman.accounts.DAO.SalaryDao;
import com.naman.accounts.DAO.SubJournalDao;
import com.naman.accounts.Model.Accounts;
import com.naman.accounts.Model.Attendance;
import com.naman.accounts.Model.Employee;
import com.naman.accounts.Model.Holidays;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.Model.SubTransaction;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class, Attendance.class, Salary.class, Holidays.class, Accounts.class,
        Journal.class, SubTransaction.class}, version = 1)

public abstract class DatabaseAdapter extends RoomDatabase {

    private static DatabaseAdapter dbAdapter;
    public abstract EmployeeDao employeeDao();
    public abstract AttendanceDao attendanceDao();
    public abstract HolidayDao holidayDao();
    public abstract SalaryDao salaryDao();
    public abstract AccountDao accountDao();
    public abstract JournalDao journalDao();
    public abstract SubJournalDao subJournalDao();

    public static DatabaseAdapter getInstance(Context context){
        if(dbAdapter == null){
            synchronized (DatabaseAdapter.class){
                dbAdapter = Room.databaseBuilder(context,
                        DatabaseAdapter.class, "accounts-db").fallbackToDestructiveMigration().build();
            }
        }
        return dbAdapter;
    }
}
