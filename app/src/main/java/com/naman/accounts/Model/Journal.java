package com.naman.accounts.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = "accountName"),
        foreignKeys = @ForeignKey(entity = Accounts.class, parentColumns = "name", childColumns = "accountName"))
public class Journal {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    @NonNull
    private String accountName;
    @NonNull
    private double amount;
    @NonNull
    private int type;
    private String remark;
    @NonNull
    private String date;
    @Ignore
    private int subValues;

    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(@NonNull String accountName) {
        this.accountName = accountName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public int getSubValues() {
        return subValues;
    }

    public void setSubValues(int subValues) {
        this.subValues = subValues;
    }

}
