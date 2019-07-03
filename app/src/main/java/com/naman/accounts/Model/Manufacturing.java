package com.naman.accounts.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Manufacturing {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String itemName;
    private double quantity;
    private long mapId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }
}
