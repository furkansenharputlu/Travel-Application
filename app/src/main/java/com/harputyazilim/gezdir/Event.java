package com.harputyazilim.gezdir;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by furkan on 25.03.2017.
 */

public class Event {
    private int userId;
    private String startTime,endTime;
    private double radius;
    private boolean exactMatch;

    public boolean isExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(boolean exactMatch) {
        this.exactMatch = exactMatch;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    ArrayList<Pair<Double,Double>> coordinates= new ArrayList<>();

    public Event(){

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public void addCoordinates(double x, double y){
        Pair<Double,Double> pair=new Pair<>(x,y);
        coordinates.add(pair);
    }
}
