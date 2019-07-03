package com.naman.accounts.adapter;

import android.content.Context;

import com.naman.accounts.DAO.AccountBalanceDao;
import com.naman.accounts.DAO.AccountDao;
import com.naman.accounts.DAO.AttendanceDao;
import com.naman.accounts.DAO.EmployeeDao;
import com.naman.accounts.DAO.HolidayDao;
import com.naman.accounts.DAO.ItemDao;
import com.naman.accounts.DAO.ItemJournalDao;
import com.naman.accounts.DAO.ItemListDao;
import com.naman.accounts.DAO.JournalDao;
import com.naman.accounts.DAO.ManufacturingDao;
import com.naman.accounts.DAO.MappingDao;
import com.naman.accounts.DAO.PaymentsDao;
import com.naman.accounts.DAO.SalaryDao;
import com.naman.accounts.DAO.SubJournalDao;
import com.naman.accounts.DAO.VendorDao;
import com.naman.accounts.Model.AccountBalance;
import com.naman.accounts.Model.Accounts;
import com.naman.accounts.Model.Attendance;
import com.naman.accounts.Model.Employee;
import com.naman.accounts.Model.Holidays;
import com.naman.accounts.Model.Item;
import com.naman.accounts.Model.ItemJournal;
import com.naman.accounts.Model.ItemList;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.Manufacturing;
import com.naman.accounts.Model.Mapping;
import com.naman.accounts.Model.Payments;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.Model.Vendor;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class, Attendance.class, Salary.class, Holidays.class, Accounts.class,
        Item.class, ItemJournal.class, ItemList.class, Vendor.class, Mapping.class, Manufacturing.class, Payments.class, AccountBalance.class,
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
    public abstract ItemDao itemDao();
    public abstract ItemJournalDao itemJournalDao();
    public abstract ItemListDao itemListDao();
    public abstract VendorDao vendorDao();
    public abstract MappingDao mappingDao();
    public abstract ManufacturingDao manufacturingDao();
    public abstract PaymentsDao paymentsDao();
    public abstract AccountBalanceDao accountBalanceDao();

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
