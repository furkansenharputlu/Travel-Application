package com.harputyazilim.gezdir;

/**
 * Created by furkan on 25.03.2017.
 */

public class Event {
    private int userId;
    private String startTime,endTime;
    private double coordinates[][];

    public Event(){}

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

    public double[][] coordinates() {
        return coordinates;
    }

    public void setLongtitudes(double[][] coordinates) {
        this.coordinates = coordinates;
    }
}
