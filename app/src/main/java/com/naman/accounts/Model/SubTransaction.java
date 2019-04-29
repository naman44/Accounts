package com.naman.accounts.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"journalId", "name"},
        foreignKeys = @ForeignKey(entity = Journal.class, parentColumns = "id", childColumns = "journalId", onDelete = CASCADE))
public class SubTransaction {

    @NonNull
    private long journalId;
    @NonNull
    private String name;
    private double amount;

    public long getJournalId() {
        return journalId;
    }

    public void setJournalId(long journalId) {
        this.journalId = journalId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
