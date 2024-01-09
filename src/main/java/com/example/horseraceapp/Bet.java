package com.example.horseraceapp;

import java.sql.Date;
import java.sql.Time;

public class Bet {
    private String horseName;
    private String rider;
    private String description;
    private Double rate;
    private String raceDate;
    public Bet(String horseName, String riderName, String riderSurname, String description, Double rate, Date date, Time time){
        this.horseName = horseName;
        this.rider = riderName+" "+riderSurname;
        this.description = description;
        this.rate = rate;
        this.raceDate = date.toString()+" "+time.toString();
    }
}
