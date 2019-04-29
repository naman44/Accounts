package com.naman.accounts.Model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.naman.accounts.adapter.DatabaseAdapter;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EmployeeListViewModel extends AndroidViewModel {

    private LiveData<List<Employee>> employeeList;
    private DatabaseAdapter appDatabase;
    private Context mContext;

    public EmployeeListViewModel(Application application){
        super(application);
        appDatabase = DatabaseAdapter.getInstance(application);
        employeeList = appDatabase.employeeDao().fetchAllEmployees();
    }

    public LiveData<List<Employee>> getEmployeeList(){
        return employeeList;
    }

    public void deleteEmployee(Employee e){
        new DeleteAsyncTask(appDatabase).execute(e);
    }

    public void upSert(Employee employee){
        new UpsertAsyncTask(appDatabase).execute(employee);
    }

    private static class UpsertAsyncTask extends AsyncTask<Employee, Void, Void>{

        private DatabaseAdapter db;

        UpsertAsyncTask(DatabaseAdapter appDataBase){
            db = appDataBase;
        }

        @Override
        protected Void doInBackground(Employee... employees) {
            long id = db.employeeDao().insertEmployee(employees[0]);
            if(id == -1){
                db.employeeDao().updateEmployee(employees[0]);
            }
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Employee, Void, Void> {

        private DatabaseAdapter db;

        DeleteAsyncTask(DatabaseAdapter appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Employee... employees) {
            db.employeeDao().deleteEmployee(employees[0].getEmpName());
            return null;
        }
    }
}
