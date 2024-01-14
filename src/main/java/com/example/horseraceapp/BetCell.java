package com.example.horseraceapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.text.DecimalFormat;

import static java.lang.Math.round;

public class BetCell extends ListCell<String> {
    private final GridPane gPane = new GridPane();
    private final HBox hBox = new HBox();
    private final Label label = new Label();
    private Label outcome = new Label();
    private Label kursL = new Label();
    private final TextField textField = new TextField();
    private final Button button = new Button("Zatwierdź");
    private Double pot = 0.0;
    private Double kurs;

    public BetCell(){
        label.setFont(new Font(16));
        textField.setPrefWidth(70.0);
        textField.setPrefHeight(8.0);
        kursL.setPrefHeight(8.0);
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
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                final DecimalFormat df = new DecimalFormat("0.00");
                if(textField.getText().isEmpty()) {
                    pot = 0.0;
                    outcome.setText(" "+pot+"zł");
                }
                else {
                    pot = kurs * Double.parseDouble(newValue);
                    outcome.setText(" " + String.valueOf(df.format(pot)) + "zł");
                }
            }
        });
        kursL.setFont(new Font(14));
        outcome.setFont(new Font(14));
        gPane.add(label,0,0);
        hBox.getChildren().addAll(textField, kursL,outcome,button);
        gPane.add(hBox,0,1);
        setGraphic(gPane);
    }
    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item,empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            String[] parts = item.split("\\|");
            label.setText(parts[0]);
            kursL.setText(" x"+parts[1]);
            kurs = Double.parseDouble(parts[1]);
            outcome.setText(" "+pot+"zł");
            setText(null);

            //fPane.setAlignment(Pos.CENTER_LEFT);
            setGraphic(gPane);
        }
    }
}
