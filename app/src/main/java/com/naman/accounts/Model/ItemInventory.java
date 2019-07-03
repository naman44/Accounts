package com.naman.accounts.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Item.class, parentColumns = "itemName",
            childColumns = "itemName", onDelete = CASCADE))
public class ItemInventory {

    @PrimaryKey
    @NonNull
    private String itemName;
    private double openingStock;
    private double closingStock;
    private double open;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(double openingStock) {
        this.openingStock = openingStock;
    }

    public double getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(double closingStock) {
        this.closingStock = closingStock;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }
}
