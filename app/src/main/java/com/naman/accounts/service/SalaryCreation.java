package com.naman.accounts.service;

import com.naman.accounts.Model.Attendance;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

class SalaryCreation {

    private Salary salaryGenerated;
    private DatabaseAdapter db;

    SalaryCreation(DatabaseAdapter db){
        this.db = db;
    }

    public Salary getSalaryGenerated() {
        return salaryGenerated;
    }

    void createSalary(Salary salaryDisplay, List<Attendance> list){
        salaryGenerated = salaryDisplay;
        int present = 0;
        int absent = 0;
        int overTime = 0;
        double timePenalty = 0;
        for(Attendance a : list){
            LocalDate date = AppUtil.formatLocalDateFromString(a.getDate());
            int holiday = db.holidayDao().fetchHolidayByDate(a.getDate());

            // check for attendance
            if(holiday > 0 || date.getDayOfWeek() == DayOfWeek.SUNDAY){
                if(a.getPresent() == 1){
                    overTime++;
                }
            }
            else{
                if(a.getPresent() == 1)
                    present++;
                else
                    absent++;
            }

            //check for time
            if(a.getPresent() == 1){
                LocalTime timeIn = AppUtil.formatLocalTimeFromString(a.getTimeIn());
                LocalTime timeOut = AppUtil.formatLocalTimeFromString(a.getTimeOut());
                timePenalty += calculatePenalty(Duration.between(timeIn, timeOut).toMillis());
            }
        }
        //getDifferenceInPay();

        //set object
        salaryGenerated.setTimePenalty(timePenalty);
        salaryGenerated.setAbsent(absent);
        salaryGenerated.setAttendance(present + overTime);
        //salaryGenerated.setAdvance(db.attendanceDao().fetchAdvanceAmount(salaryDisplay.getEmpName(), salaryGenerated.getMonth() + "/01"));
        salaryGenerated.setAdvance(db.accountDao().fetchAccountByName(salaryGenerated.getEmpName()).getClosingBalance());

        int adjustment = getMonthAdjustment(present, absent);
        double dailyWage = salaryDisplay.getSalary()/30;
        double hourlyWage = dailyWage/8;

        double salary = dailyWage*(salaryGenerated.getAttendance() + salaryGenerated.getRestDays() + adjustment)
                - (timePenalty*hourlyWage) - salaryGenerated.getAdvance();

        salaryGenerated.setSalaryToPay(salary);
    }

    private int getMonthAdjustment(int present, int absent){
        int adjustment = 0;
        if(present <= 6){
            salaryGenerated.setRestDays(0);
            adjustment = 0;
        }
        else{
            int holidayCount = db.holidayDao().fetchHolidayForMonth(salaryGenerated.getMonth());
            YearMonth y = YearMonth.parse(salaryGenerated.getMonth(), DateTimeFormatter.ofPattern("yyyy/MM"));
            long sundayCount = IntStream.rangeClosed(1, y.lengthOfMonth())
                    .mapToObj(day -> LocalDate.of(y.getYear(), y.getMonth(), day))
                    .filter(date -> date.getDayOfWeek() == DayOfWeek.SUNDAY).count();

            int rest = present/6;
            int proxyTotal = (int) (present + holidayCount + sundayCount);
            if(y.lengthOfMonth() - proxyTotal <= 2 && sundayCount > 4){
                rest += 1;
            }
            salaryGenerated.setRestDays(rest + holidayCount);

            if((proxyTotal + absent) == y.lengthOfMonth()){
                adjustment = (30 - (proxyTotal + absent));
            }

        }
        return adjustment;
    }

    private double calculatePenalty(long difference){
        double timeSpent = (double) difference/3600000;
        double diff = 8.25 - timeSpent;
        int differential = (int) Math.floor(diff/0.5) + 1;
        return differential*0.5;
    }

//    private void getDifferenceInPay(){
//        double diff = 0;
//        String month = AppUtil.adjustMonth(salaryGenerated.getMonth(), "yyyy/MM", -1);
//        Salary salaryLast = db.salaryDao().fetchSalary(month, salaryGenerated.getEmpName());
//        if(salaryLast != null && salaryLast.getPayDate() != null && !salaryLast.getPayDate().isEmpty()){
//            diff = salaryLast.getSalaryToPay() - salaryLast.getAmountPaid();
//        }
//        salaryGenerated.setDifference(diff);
//    }
}
