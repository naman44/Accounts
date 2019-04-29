package com.naman.accounts.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Holidays {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String month;
    private String date;
    private String type;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
