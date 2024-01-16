package com.example.horseraceapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.text.DecimalFormat;

import static java.lang.Math.round;

public class BetCellFactory extends ListCell<BetCell> {
    private final GridPane gPane = new GridPane();
    private final HBox hBox = new HBox();
    private final Label label = new Label();
    private Label outcome = new Label();
    private Label kursL = new Label();
    private TextField textField = new TextField();
    private Button button;
    private Double pot = 0.0;
    private Double kurs;
    private Double balance;
    private int id_udzialu;
    UserMenuController userMenuController;

    public BetCellFactory() {
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
                    outcome.setText(" " +df.format(pot) + "zł");
                }
            }
        });
        kursL.setFont(new Font(14));
        outcome.setFont(new Font(14));
        setGraphic(gPane);
    }
    @Override
    protected void updateItem(BetCell betCell, boolean empty){
        super.updateItem(betCell,empty);
        if (empty || betCell == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.button = betCell.button;
            String[] parts = betCell.item.split("\\|");
            String[] id = parts[0].split("!");
            id_udzialu = Integer.parseInt(id[0]);
            betCell.id_udzialu = this.id_udzialu;
            System.out.println(id_udzialu);
            label.setText(id[1]);
            kursL.setText(" x"+parts[1]);
            kurs = Double.parseDouble(parts[1]);
            betCell.kurs = this.kurs;
            outcome.setText(" "+pot+"zł");
            betCell.label = this.label;
            betCell.textField = this.textField;
            betCell.outcome = this.outcome;
            betCell.kursL = this.kursL;
            if(hBox.getChildren().size() < 4) {
                gPane.add(betCell.label, 0, 0);
                hBox.getChildren().addAll(betCell.textField, betCell.kursL, betCell.outcome);
                gPane.add(hBox, 0, 1);
                hBox.getChildren().add(3, betCell.button);
            }
            setText(null);

            setGraphic(gPane);
        }
    }

    public Double getBet(){
        return Double.parseDouble(textField.getText());
    }

    public Button getButton(){
        return button;
    }

    public void setButton(Button button){
        this.button = button;
    }
}
