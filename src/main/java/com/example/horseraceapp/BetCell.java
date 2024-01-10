package com.example.horseraceapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.sql.Date;
import java.sql.Time;

public class BetCell extends ListCell<String> {
    private final HBox hbox = new HBox();
    private final Label label = new Label();
    private final TextField textField = new TextField();
    private final Button button = new Button("Zatwierdź");

    public BetCell(){
        hbox.getChildren().addAll(label, textField,button);
        setGraphic(hbox);
    }
    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item,empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            label.setText(item);
            setText(null);
            setGraphic(hbox);
        }
    }
}
