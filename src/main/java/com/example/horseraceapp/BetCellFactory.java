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
    private final Label outcome = new Label();
    private final Label kursL = new Label();
    private Double pot = 0.0;
    private Double kurs;
    boolean l = true;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public BetCellFactory() {
        label.setFont(new Font(16));
        kursL.setPrefHeight(8.0);
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
            String[] parts = betCell.item.split("\\|");
            String[] id = parts[0].split("!");
            int id_udzialu = Integer.parseInt(id[0]);
            betCell.id_udzialu = id_udzialu;
            label.setText(id[1]);
            kursL.setText(" x"+parts[1]);
            kurs = Double.parseDouble(parts[1]);
            betCell.kurs = this.kurs;
            outcome.setText(" "+df.format(pot)+"zł");
            betCell.label = this.label;
            //@TODO fix bug where texfield appears full when it shouldnt
            betCell.outcome = this.outcome;
            betCell.kursL = this.kursL;
            if(l) {
                gPane.add(betCell.label, 0, 0);
                hBox.getChildren().addAll(betCell.textField, betCell.kursL, betCell.outcome);
                gPane.add(hBox, 0, 1);
                hBox.getChildren().add(3, betCell.button);
                l = false;
            }
            setText(null);

            setGraphic(gPane);
        }
    }
}
