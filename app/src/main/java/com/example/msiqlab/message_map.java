package com.example.msiqlab;

public class message_map {
    //大標題
    private String tital;
    //時間
    private String time;
    //內容
    private String conn;

    //建構式
    public message_map(String totital, String totime, String toconn) {
        tital = totital;
        time = totime;
        conn = toconn;
    }
    public String gettital() {
        return tital;
    }
    public String gettime() {
        return time;
    }
    public String getconn() {
        return conn;
    }
}
