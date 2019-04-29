package com.naman.accounts.service;

import com.naman.accounts.Model.Attendance;
import com.naman.accounts.Model.Salary;
import com.naman.accounts.adapter.DatabaseAdapter;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SalaryService {

    private DatabaseAdapter db;
    public SalaryService(DatabaseAdapter db){
        this.db = db;
    }

    public String getMonthForResume(){
        String monthValue = "";
        String monthUnpaid = db.salaryDao().getMonthUnpaid();
        if(monthUnpaid == null || monthUnpaid.isEmpty()){
            String monthPaid = db.salaryDao().getMonthPaid();
            if(monthPaid == null || monthPaid.isEmpty()){
                String date = db.attendanceDao().fetchFirstAttendanceDate();
                if(date != null && !date.isEmpty()){
                    LocalDate l = AppUtil.formatLocalDateFromString(date);
                    monthValue = DateTimeFormatter.ofPattern("yyyy/MM").format(l);
                }
            }
            else{
                monthValue = monthPaid;
            }
        }
        else {
            monthValue = monthUnpaid;
        }
        return monthValue;
    }

    public List<Salary> generateSalaryByMonth(String month){
        List<Salary> listUnpaid = db.salaryDao().getUnpaidSalary(month);
        List<Salary> listToReturn = new ArrayList<>(listUnpaid);
        if(!listUnpaid.isEmpty()){
            for(Salary sd : listUnpaid){
                if(sd.getId() <= 0){
                    salaryMethod(sd, month);
                    if(sd.getId() <= 0){
                        listToReturn.remove(sd);
                    }
                }
            }
        }
        return listToReturn;
    }

    public List<Salary> updateSalaryBatch(String month){
        List<Salary> list = db.salaryDao().fetchSalaryForMonth(month);
        List<Salary> listToReturn = new ArrayList<>();
        for(Salary sd : list){
            if(sd.getId() != 0){
                if(sd.getPayDate() == null ||sd.getPayDate().isEmpty()){
                    salaryMethod(sd, month);
            }
                listToReturn.add(sd);
            }
        }
        return listToReturn;
    }

    public void updateSalarySingle(String empName, String month){
        Salary salary = db.salaryDao().fetchSalary(month, empName);
        salaryMethod(salary, month);
    }

    private void salaryMethod(@NotNull Salary sd, String month){
        List<Attendance> attendanceList = db.attendanceDao().getAttendanceForMonth(month + "%", sd.getEmpName());
        if(attendanceList != null && !attendanceList.isEmpty()){
            sd.setMonth(month);
            new SalaryCreation(db).createSalary(sd, attendanceList);
            long id = upSertSalary(sd);
            if(sd.getId() <= 0)
                sd.setId(id);
        }
    }

    private long upSertSalary(Salary salary){
        long id = 0;
        try{
           id = db.salaryDao().insertSalary(salary);
        }catch (Exception e){
            id = db.salaryDao().updateSalary(salary);
        }
        return id;
    }

    public void updateBatchPayment(List<Salary> list){
        for(Salary sd : list){
            Salary sal = db.salaryDao().fetchSalary(sd.getId());
            if(sal.getPayDate() == null || sal.getPayDate().isEmpty()){
                sal.setPayDate(AppUtil.formatDate(LocalDate.now()));
                sal.setAmountPaid(sd.getSalaryToPay());
                db.salaryDao().updateSalary(sal);
            }
        }
    }

}
