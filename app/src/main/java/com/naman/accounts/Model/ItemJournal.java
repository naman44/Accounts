package com.naman.accounts.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = @Index("vendorName"),
        foreignKeys = @ForeignKey(entity = Vendor.class, parentColumns = "vendorName", childColumns = "vendorName", onDelete = CASCADE))
public class ItemJournal {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long manufactureId;
    private int entryType;
    private String date;
    private String invoiceNumber;
    private String vendorName;
    private double totalAmount;
    private int cgstRate;
    private int sgstRate;
    private int igstRate;
    private double cgstAmount;
    private double sgstAmount;
    private double igstAmount;
    private double balanceAmount;
    private double amountWithGst;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(long manufactureId) {
        this.manufactureId = manufactureId;
    }

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCgstRate() {
        return cgstRate;
    }

    public void setCgstRate(int cgstRate) {
        this.cgstRate = cgstRate;
    }

    public int getSgstRate() {
        return sgstRate;
    }

    public void setSgstRate(int sgstRate) {
        this.sgstRate = sgstRate;
    }

    public int getIgstRate() {
        return igstRate;
    }

    public void setIgstRate(int igstRate) {
        this.igstRate = igstRate;
    }

    public double getCgstAmount() {
        return cgstAmount;
    }

    public void setCgstAmount(double cgstAmount) {
        this.cgstAmount = cgstAmount;
    }

    public double getSgstAmount() {
        return sgstAmount;
    }

    public void setSgstAmount(double sgstAmount) {
        this.sgstAmount = sgstAmount;
    }

    public double getIgstAmount() {
        return igstAmount;
    }

    public void setIgstAmount(double igstAmount) {
        this.igstAmount = igstAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getAmountWithGst() {
        return amountWithGst;
    }

    public void setAmountWithGst(double amountWithGst) {
        this.amountWithGst = amountWithGst;
    }
}
