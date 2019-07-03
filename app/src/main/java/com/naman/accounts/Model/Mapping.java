package com.naman.accounts.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = @Index(value = "productName", name = "productName"),
        foreignKeys = @ForeignKey(entity = Item.class, parentColumns = "itemName", childColumns = "productName", onDelete = CASCADE))
public class Mapping {

    @PrimaryKey(autoGenerate = true)
    private long mapId;
    @NonNull
    private String mapName;
    @NonNull
    private String productName;
    private double quantityItem;
    private String item1;
    private double quantity1;
    private String item2;
    private double quantity2;
    private String item3;
    private double quantity3;

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getProductName() {
        return productName;
    }

    public double getQuantityItem() {
        return quantityItem;
    }

    public void setQuantityItem(double quantityItem) {
        this.quantityItem = quantityItem;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public double getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(double quantity1) {
        this.quantity1 = quantity1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public double getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(double quantity2) {
        this.quantity2 = quantity2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public double getQuantity3() {
        return quantity3;
    }

    public void setQuantity3(double quantity3) {
        this.quantity3 = quantity3;
    }
}
