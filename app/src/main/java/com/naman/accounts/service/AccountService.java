package com.naman.accounts.service;

import com.naman.accounts.Model.Accounts;
import com.naman.accounts.Model.ItemJournal;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.Vendor;
import com.naman.accounts.adapter.DatabaseAdapter;

public class AccountService {

    private DatabaseAdapter db;

    public AccountService(DatabaseAdapter db){
        this.db = db;
    }

    private void updateAccountBalance(String accountName, double amount){
        Accounts acc = db.accountDao().fetchAccountByName(accountName);
        double update = acc.getClosingBalance() + amount;
        acc.setClosingBalance(update);
        db.accountDao().updateAccount(acc);
    }

    long newJournalEntry(ItemJournal journal){
        long id = db.itemJournalDao().insertEntry(journal);
        if(journal.getEntryType() == AppConstants.JOURNAL_TYPE_SALE){
            updateAccountBalance(journal.getVendorName(), journal.getAmountWithGst()*-1);
        }
        else
            updateAccountBalance(journal.getVendorName(), journal.getAmountWithGst());
        return id;
    }

    public void updateItemJournalForPayment(String vendorName, int entryType, double amount){
        if(entryType == AppConstants.JOURNAL_TYPE_PURCHASE)
            updateAccountBalance(vendorName, amount*-1);
        else
            updateAccountBalance(vendorName, amount);
    }

    public long newJournalEntry(Journal journal){
        long id = db.journalDao().insertTransaction(journal);
        if(journal.getType() == AppConstants.INT_CREDIT){
            updateAccountBalance(journal.getAccountName(), journal.getAmount()*-1);
        }
        else
            updateAccountBalance(journal.getAccountName(), journal.getAmount());
        return id;
    }

    public int deleteJournalEntry(Journal journal){
        int count = db.journalDao().deleteTransaction(journal);
        if(count > 0){
            if(journal.getType() == AppConstants.INT_CREDIT)
                updateAccountBalance(journal.getAccountName(), journal.getAmount());
            else
                updateAccountBalance(journal.getAccountName(), journal.getAmount()*-1);
        }
        return count;
    }

    void createVendor(Vendor vendor){
        db.vendorDao().insertVendor(vendor);
        Accounts acc = new Accounts();
        acc.setName(vendor.getVendorName());
        acc.setType(AppConstants.AC_TYPE_VENDOR);
        db.accountDao().insertAccount(acc);
    }


}
