package com.db;

public class XgDate {
    public int year=1970;
    public int month=1;
    public int day=1;

    public int weekDay=1;
    public int hour=0;
    public int mi=0;
    public int sec=0;
    public int usec=0;
    static int maxMonth =10;
    static int maxDay =10;

    public String getDateString(){
        String monthStr;
        String dayStr;

        if (month < maxMonth) {
            monthStr = "0" + month;
        } else {
            monthStr = Integer.toString(month);
        }
        if (day < maxDay) {
            dayStr = "0" + day;
        } else {
            dayStr = Integer.toString(day);
        }
        return (year + "-" + monthStr + "-" + dayStr);
    }
    public  String getDtString(){
        String dtstr=String.format("%04d-%02d-%02d %02d:%02d:%02d.%06d", this.year,this.month,this.day,this.hour,this.mi,this.sec,usec);
        return dtstr;
    }

    public  String getTime(){
        String dtstr=String.format("%02d:%02d:%02d.%06d", this.hour,this.mi,this.sec,usec);
        return dtstr;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getWeekDay() {
        return weekDay;
    }
    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMi() {
        return mi;
    }
    public void setMi(int mi) {
        this.mi = mi;
    }
    public int getSec() {
        return sec;
    }
    public void setSec(int sec) {
        this.sec = sec;
    }
    public int getUsec() {
        return usec;
    }
    public void setUsec(int usec) {
        this.usec = usec;
    }
}
