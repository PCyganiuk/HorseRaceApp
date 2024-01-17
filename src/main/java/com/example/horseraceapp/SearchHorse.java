package com.example.horseraceapp;

import javafx.beans.property.*;

public class SearchHorse {
    private final StringProperty name;
    private final IntegerProperty age;
    private final IntegerProperty compRaces;
    private final IntegerProperty wins;
    private final IntegerProperty loses;
    private final DoubleProperty avgOdds;
    public SearchHorse(String name,int compRaces,int wins, int loses, double avgOdds, int age){
        this.name = new SimpleStringProperty(name);
        this.compRaces = new SimpleIntegerProperty(compRaces);
        this.age = new SimpleIntegerProperty(age);
        this.wins = new SimpleIntegerProperty(wins);
        this.loses = new SimpleIntegerProperty(loses);
        this.avgOdds = new SimpleDoubleProperty(avgOdds);
    }
    public StringProperty nameProperty() {
        return name;
    }
    public IntegerProperty compRacesProperty() {
        return compRaces;
    }
    public IntegerProperty winsProperty() {
        return wins;
    }
    public IntegerProperty losesProperty() {
        return loses;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public DoubleProperty avgOddsProperty() {
        return avgOdds;
    }
}
