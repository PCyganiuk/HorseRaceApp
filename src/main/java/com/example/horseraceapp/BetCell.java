package com.example.horseraceapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BetCell {
    String item;
    int id_udzialu;
    Double kurs;
    Button button;
    Label label;
    Label outcome;
    Label kursL;
    TextField textField;

    public BetCell(String item, Button button){
        this.item = item;
        this.button = button;
    }
    //@TODO przerobić BetCellFactory tak żeby atrybuty były w BetCell
}
