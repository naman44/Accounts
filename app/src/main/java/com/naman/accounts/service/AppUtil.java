package com.naman.accounts.service;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.naman.accounts.Model.Journal;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppUtil {

    public static String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.dateFormat);
        return formatter.format(date);
    }

    public static String formatDateToVIew(String date){
        if(date == null || date.isEmpty()){
            return "";
        }
        LocalDate d = LocalDate.parse(date, DateTimeFormatter.ofPattern(AppConstants.dateFormat));
        return DateTimeFormatter.ofPattern(AppConstants.dateFormatEasy).format(d);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(AppConstants.dateFormat, getLocale());
        return df.format(date);
    }

    public static LocalDate formatLocalDateFromString(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.dateFormat);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate formatLocalDateFromString(String date, String dateFormat){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(date, formatter);
    }

    public static String formatYearMonth(String date, String format){
        LocalDate d = formatLocalDateFromString(date);
        YearMonth ym = YearMonth.from(d);
        return ym.format(DateTimeFormatter.ofPattern(format));
    }

    public static Date formatDateFromString(String date) {
        try{
            SimpleDateFormat df = new SimpleDateFormat(AppConstants.dateFormat, getLocale());
            return df.parse(date);
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return null;
    }

    public static LocalTime formatLocalTimeFromString(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.timeFormat, getLocale());
        return LocalTime.parse(time, formatter);
    }

    public static Date formatTimeFromString(String time) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat(AppConstants.timeFormat, getLocale());
        return df.parse(time);
    }

    private static Locale getLocale(){
        return Locale.getDefault();
    }

    public static Locale getLocale(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String country = null;
        if(tm != null)
            country = tm.getNetworkCountryIso();
        if(country != null && !country.isEmpty()) {
            return new Locale("en", country);
        }
        else return Locale.getDefault();
    }

    private static String getCurrencySymbol(Context context){
        Locale locale = getLocale(context);
        if(locale != null){
            Currency currency = Currency.getInstance(getLocale(context));
            return currency.getSymbol();
        }
        else
            return "";
    }

    public static String getAmountWithSymbol(Context context, double amount){
        String am = new DecimalFormat("#.##").format(amount);
        return getCurrencySymbol(context) + am;
    }

    public static String getAmountWithSymbol(Context context, String amount){
        return getCurrencySymbol(context) + amount;
    }

    public static double getAmountSansSymbol(Context context, String amount){
        amount = amount.replace(getCurrencySymbol(context), "");
        return Double.parseDouble(amount);
    }

    public static String adjustMonth(String monthValue, String pattern, int adjust){
        YearMonth y = YearMonth.parse(monthValue, DateTimeFormatter.ofPattern(pattern));
        if(adjust > 0){
            y = y.plusMonths(1);
        }
        else
            y = y.minusMonths(1);
        return y.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static int getJournalType(String value){
        int type = -1;
        if(value.equalsIgnoreCase("SALE")){
            type = AppConstants.JOURNAL_TYPE_SALE;
        }
        else if(value.equalsIgnoreCase("PURCHASE")){
            type = AppConstants.JOURNAL_TYPE_PURCHASE;
        }
        return type;
    }

    public static int getFinancialYear(String date){
        LocalDate inputDate = formatLocalDateFromString(date);
        LocalDate fyDate = LocalDate.of(inputDate.getYear(), 4, 1);
        if(inputDate.isBefore(fyDate))
            return inputDate.getYear() - 1;
        else
            return inputDate.getYear();
    }

    public static double getValue(List<Journal> journalList){
        double total = 0;
        for(Journal j : journalList){
            if(j.getType() == AppConstants.INT_CREDIT)
                total += j.getAmount()*-1;
            else
                total += j.getAmount();
        }
        return total;
    }

}
