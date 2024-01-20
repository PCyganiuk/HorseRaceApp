package com.example.horseraceapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Font;

import java.text.DecimalFormat;

public class BetCell {
    String item;
    int id_udzialu;
    Double kurs;
    Double pot = 0.0;
    Button button;
    Label outcome = new Label(" = 0.0zł");
    Label kursL = new Label();
    TextField textField = new TextField();
    private final DecimalFormat df = new DecimalFormat("0.00");

    public BetCell(String item, Button button){
        String[] split = item.split("!");
        this.id_udzialu = Integer.parseInt(split[0]);
        this.button = button;
        this.button.setText("Zatwierdź");
        String[] split2 = split[1].split("\\|");
        this.kurs = Double.parseDouble(split2[1]);
        this.kursL.setText(" x"+ kurs);
        this.item = split2[0];
        this.outcome.setFont(new Font(14));
        textField.setPrefWidth(70.0);
        textField.setPrefHeight(8.0);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("zmiana");
            if(textField.getText().isEmpty()) {
                pot = 0.0;
                outcome.setText(" = "+pot+"zł");
            }
            else {
                kurs = Double.parseDouble(kursL.getText().replace("x",""));
                pot = kurs * Double.parseDouble(newValue);
                outcome.setText(" = " +df.format(pot) + "zł");
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
