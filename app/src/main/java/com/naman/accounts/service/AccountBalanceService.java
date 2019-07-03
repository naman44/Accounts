package com.naman.accounts.service;

import com.naman.accounts.Model.AccountBalance;
import com.naman.accounts.Model.Journal;
import com.naman.accounts.adapter.DatabaseAdapter;

import java.time.LocalDate;
import java.util.List;

public class AccountBalanceService {

    private DatabaseAdapter db;

    public AccountBalanceService(DatabaseAdapter db){
        this.db = db;
    }

    public void initAccountBalances(){
        List<String> accounts = db.accountDao().fetchAccountNames();
        for(String name : accounts){
            initAccountOpening(name);
        }
    }

    private void initAccountOpening(String name){
        Journal j = db.journalDao().fetchFirstEntryForAccount(name);
        if(j != null){
            int fyYear = AppUtil.getFinancialYear(j.getDate());
            int present = db.accountBalanceDao().checkAccountBalanceEntry(fyYear+"", j.getAccountName());
            if(present <= 0) {
                initBalance(j, fyYear);
            }
        }
    }

    private void initBalance(Journal j, int fyYear){
        int currentYear = AppUtil.getFinancialYear(AppUtil.formatDate(LocalDate.now()));
        double closingBalance = 0;
        do {
            String startDate = AppUtil.formatDate(LocalDate.of(fyYear, 4, 1));
            String lastDate = AppUtil.formatDate(LocalDate.of(fyYear + 1, 4, 1).minusDays(1));
            AccountBalance bal = new AccountBalance();
            bal.setAccountName(j.getAccountName());
            bal.setPeriod(fyYear + "");
            bal.setOpeningBalance(closingBalance);
            closingBalance = AppUtil.getValue(db.journalDao().fetchBalanceForFy(startDate, lastDate, j.getAccountName()));
            if (fyYear != currentYear) {
                bal.setClosingBalance(closingBalance);
                fyYear++;
            }
            db.accountBalanceDao().insertBalance(bal);
        } while (fyYear != currentYear);
    }
}
