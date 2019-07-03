package com.naman.accounts.service;

public class AppConstants {

    static String dateFormat = "yyyy/MM/dd";
    static String dateFormatEasy = "dd MMM";
    static String timeFormat="HH:mm";
    public static String startingTime = "09:00";
    public static String endTime = "17:30";

    // expense type
    public static int INT_CREDIT = 0;
    public static int INT_DEBIT = 1;

    // account type
    public static int AC_TYPE_EXPENSE = 0;
    public static int AC_TYPE_SALARY = 1;
    public static int AC_TYPE_VENDOR = 2;

    public static int JOURNAL_TYPE_SALE = 1;
    public static int JOURNAL_TYPE_PURCHASE = 0;
    public static int JOURNAL_TYPE_MANUFACTURE = 2;
    public static int JOURNAL_TYPE_CONSUME = 3;

}
