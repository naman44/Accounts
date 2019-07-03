package com.naman.accounts.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"journalId", "itemName"},
        indices = @Index(value = "itemName"),
        foreignKeys = {@ForeignKey(entity = Item.class, parentColumns = "itemName", childColumns = "itemName", onDelete = CASCADE),
                        @ForeignKey(entity = ItemJournal.class, parentColumns = "id", childColumns = "journalId", onDelete = CASCADE)})
public class ItemList {

    private long journalId;
    @NonNull
    private String itemName;
    private double quantity;
    private double price;
    private double amount;

    public long getJournalId() {
        return journalId;
    }

    public void setJournalId(long journalId) {
        this.journalId = journalId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
