package com.example.horseraceapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.text.DecimalFormat;

public class BetCell {
    String item;
    int id_udzialu;
    Double kurs;
    Double pot = 0.0;
    Button button;
    Label label;
    Label outcome;
    Label kursL;
    TextField textField = new TextField();
    private final DecimalFormat df = new DecimalFormat("0.00");

    public BetCell(String item, Button button){
        this.item = item;
        this.button = button;
        textField.setPrefWidth(70.0);
        textField.setPrefHeight(8.0);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(textField.getText().isEmpty()) {
                pot = 0.0;
                outcome.setText(" "+pot+"zł");
            }
            else {
                String k = kursL.getText().replace("x","");
                kurs = Double.parseDouble(k);
                pot = kurs * Double.parseDouble(newValue);
                outcome.setText(" " +df.format(pot) + "zł");
            }
        });
        TextFormatter<String> numericFormat = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*\\.?[0-9]*")) {
                return change;  // Allow the change
            }
            else {
                return null;    // Reject the change
            }
        });
        textField.setTextFormatter(numericFormat);
    }
}
