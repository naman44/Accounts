package com.naman.accounts.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Payments {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long journalId;
    private double amount;
    private String date;
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJournalId() {
        return journalId;
    }

    public void setJournalId(long journalId) {
        this.journalId = journalId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
