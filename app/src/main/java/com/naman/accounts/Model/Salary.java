package com.naman.accounts.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = @Index(value = "empName"),
        foreignKeys = @ForeignKey(entity = Employee.class, parentColumns = "empName", childColumns = "empName", onDelete = CASCADE))
public class Salary {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String empName;
    private String month;
    private double salary;
    private double salaryToPay;
    private String payDate;
    private double amountPaid;
    private int attendance;
    private int absent;
    private int restDays;
    private double timePenalty;
    private double advance;
    private double difference;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Ignore
    public void setMonth(int year, int month){
        this.month = year + "/" + month;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalaryToPay() {
        return salaryToPay;
    }

    public void setSalaryToPay(double salaryToPay) {
        this.salaryToPay = salaryToPay;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getRestDays() {
        return restDays;
    }

    public void setRestDays(int restDays) {
        this.restDays = restDays;
    }

    public double getTimePenalty() {
        return timePenalty;
    }

    public void setTimePenalty(double timePenalty) {
        this.timePenalty = timePenalty;
    }

    public double getAdvance() {
        return advance;
    }

    public void setAdvance(double advance) {
        this.advance = advance;
    }
}
