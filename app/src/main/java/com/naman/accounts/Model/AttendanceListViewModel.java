package com.naman.accounts.Model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.naman.accounts.adapter.DatabaseAdapter;
import com.naman.accounts.service.AttendanceService;

import java.util.List;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AttendanceListViewModel extends AndroidViewModel {

    private DatabaseAdapter appDatabase;
    private Context mContext;
    private int modeValue;

    public AttendanceListViewModel(Application application){
        super(application);
        appDatabase = DatabaseAdapter.getInstance(application);
        mContext = application;
    }

    public LiveData<List<Attendance>> getAttendanceList(String date){
        LiveData<List<Attendance>> attendanceList;
        new Thread(()->
            modeValue = new AttendanceService().getAttendanceListForDate(date, mContext)).start();
        attendanceList = appDatabase.attendanceDao().fetchAttendanceForDateLive(date);
        return attendanceList;
    }

    public void updateAttendance(Attendance attendance){
        new UpdateAsyncTask(appDatabase).execute(attendance);
    }

    public void deleteAttendance(String date){
        new DeleteAsyncTask(appDatabase).execute(date);
    }

    public int getModeValue() {
        return modeValue;
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void>{

        private DatabaseAdapter db;

        DeleteAsyncTask(DatabaseAdapter appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(String... params) {
            db.attendanceDao().deleteAttendanceForDate(params[0]);
            return null;
        }

    }

    private static class UpdateAsyncTask extends AsyncTask<Attendance, Void, Void> {

        private DatabaseAdapter db;

        UpdateAsyncTask(DatabaseAdapter appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Attendance... attendances) {
            db.attendanceDao().updateAttendance(attendances[0]);
            return null;
        }
    }
}
