package com.naman.accounts.service;

import com.naman.accounts.Model.Holidays;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.util.HashMap;
import java.util.List;

public class HolidayListPump {

    private static HashMap<String, List<Holidays>> holidayMap;

    public static HashMap<String, List<Holidays>> getHolidaysList(DatabaseAdapter db){
        holidayMap = new HashMap<>();
        List<String> monthList = db.holidayDao().fetchHolidayMonths();
        for(String month : monthList){
            List<Holidays> holidaysList = db.holidayDao().fetchHolidayListByMonth(month);
            holidayMap.put(month, holidaysList);
        }
        return holidayMap;
    }
}
