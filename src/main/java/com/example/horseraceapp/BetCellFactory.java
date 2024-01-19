package com.example.horseraceapp;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.text.DecimalFormat;


public class BetCellFactory extends ListCell<BetCell> {
    private final GridPane gPane = new GridPane();
    private final HBox hBox = new HBox();
    private final Label label = new Label();
    private Label outcome = new Label();
    private Label kursL = new Label();


    private TextField textField;
    private Button button;

    public BetCellFactory() {
        label.setFont(new Font(16));
        kursL.setPrefHeight(8.0);
        kursL.setFont(new Font(14));
        setGraphic(gPane);
    }
    @Override
    protected void updateItem(BetCell betCell, boolean empty){
        super.updateItem(betCell,empty);
        if (empty || betCell == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.setText(betCell.item);
            this.kursL.setText(betCell.kursL.getText());
            this.outcome = betCell.outcome;
            this.textField = betCell.textField;
            this.button = betCell.button;
            gPane.getChildren().clear();
            hBox.getChildren().clear();
            gPane.add(this.label, 0, 0);
            hBox.getChildren().addAll(this.textField, this.kursL, this.outcome);
            gPane.add(hBox, 0, 1);
            hBox.getChildren().add(3, this.button);
            setText(null);

            setGraphic(gPane);
        }
    }
}
