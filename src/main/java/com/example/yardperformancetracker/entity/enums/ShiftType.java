package com.example.yardperformancetracker.entity.enums;

public enum ShiftType {
    MORNING("06:30 - 14:30"),
    TWILIGHT("11:30 - 19:30"),
    LATE("14:30 - 22-30"),
    DOWN("19:30 - 03:30"),
    NIGHT("22.30 - 06:30");
    private final String timeRange;
    ShiftType(String timeRange){
        this.timeRange = timeRange;
    }
    public String getTimeRange(){
        return timeRange;
    }

}
