package com.punarvastra.entity;

/**
 * Entity class for weekly analytics data used in charts
 */
public class WeeklyData {
    private String date;
    private int count;

    public WeeklyData() {}

    public WeeklyData(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}