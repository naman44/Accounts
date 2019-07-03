package com.naman.accounts.service;

import com.naman.accounts.Model.Journal;
import com.naman.accounts.Model.SubTransaction;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.time.LocalDate;
import java.util.List;

public class JournalService {

    private DatabaseAdapter db;
    public JournalService(DatabaseAdapter db){
        this.db = db;
    }

    public double calculateOpeningBalance(String date, String account){
        int fyYear = AppUtil.getFinancialYear(date);
        double opening = db.accountBalanceDao().fetchOpeningByDate(fyYear+"", account);
        String startDate = AppUtil.formatDate(LocalDate.of(fyYear, 4, 1));
        String lastDate = AppUtil.formatDate(AppUtil.formatLocalDateFromString(date).minusDays(1));
        double value = AppUtil.getValue(db.journalDao().fetchBalanceForFy(startDate,lastDate,account));
        return value + opening;
    }

    public void createJournal(String account, String date, boolean checked, String remark, double amount, List<SubTransaction> subList){
        Journal j = new Journal();
        j.setAccountName(account);
        j.setDate(date);
        j.setRemark(remark);
        if(checked)
            j.setType(AppConstants.INT_DEBIT);
        else
            j.setType(AppConstants.INT_CREDIT);
        j.setAmount(amount);
        long id = new AccountService(db).newJournalEntry(j);
        if(subList != null && subList.size() > 0){
            for(SubTransaction tr : subList){
                tr.setJournalId(id);
                db.subJournalDao().insertSub(tr);
            }
        }
    }
}
