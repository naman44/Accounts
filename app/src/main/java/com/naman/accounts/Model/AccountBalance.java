package com.naman.accounts.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = @Index(name = "accountName", value = "accountName"),
        foreignKeys = @ForeignKey(entity = Accounts.class, parentColumns = "name", childColumns = "accountName", onDelete = CASCADE))
public class AccountBalance {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String accountName;
    private String period;
    private double openingBalance;
    private double closingBalance;

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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public double getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(double closingBalance) {
        this.closingBalance = closingBalance;
    }
}
