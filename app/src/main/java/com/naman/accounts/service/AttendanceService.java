package com.naman.accounts.service;

import android.content.Context;

import com.naman.accounts.Model.Attendance;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.time.DayOfWeek;
import java.util.List;

public class AttendanceService {

    /**
     * return Types
     *  0 = empty db
     *  1 =
     */
    private int returnType = -1;

    public int getAttendanceListForDate(String date, Context context){
        int holidayCheck = DatabaseAdapter.getInstance(context).holidayDao().fetchHolidayByDate(date);
        DayOfWeek dayOfWeek = AppUtil.formatLocalDateFromString(date).getDayOfWeek();
        if(holidayCheck == 0 && dayOfWeek != DayOfWeek.SUNDAY) {
            generateAttendance(date, context);
        }
        else if(holidayCheck > 0){
            returnType = 1;
        }
        if(dayOfWeek == DayOfWeek.SUNDAY){
            returnType = 2;
        }
        return returnType;
    }

    public void generateAttendance(String date, Context context){
        List<Attendance> aList = DatabaseAdapter.getInstance(context).attendanceDao().fetchAtt(date);
        if(aList == null || aList.isEmpty())
            returnType = 0;
        if(aList != null && !aList.isEmpty()){
            for (Attendance a : aList) {
                if (a.getDate() == null || a.getDate().isEmpty()) {
                    a.setPresent(1);
                    a.setDate(date);
                    a.setTimeOut(AppUtil.endTime);
                    a.setTimeIn(AppUtil.startingTime);
                    DatabaseAdapter.getInstance(context).attendanceDao().insertAttendance(a);
                }
            }
        }
    }

    /**
     * get attendance from DB
     * 1. if no entry, generate for current date
     * 2. if entry, check dateOnPage != today's date, call getAttendanceListForDate(dateFromPage)
     * 3. if entry and dateFromPage == today,
     *      a. check date from db == today, if yes, return today's attendance
     *      b. if dateFromPage > db, cycle through all dates and create attendance till dateInDb = today;
     *
     *
     */
    public void attendanceSkipper(String dateOnPage, Context context){

    }
}
