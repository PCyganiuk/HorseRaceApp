package com.example.horseraceapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    @FXML
    Label nick;
    @FXML
    Label money;
    Connection con;
    Statement stm;
    String username;
    Double balance;

    public void setNickandBal(String username){
        this.username = username;
        nick.setText(this.username);
        System.out.println(username);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace","uzytkownik","user123");
            stm = con.createStatement();
            String sql = "SELECT saldo_konta FROM uzytkownicy WHERE nazwa_uzytkownika = ?";
            PreparedStatement prp = con.prepareStatement(sql);
            prp.setString(1,this.username);
            ResultSet rs = prp.executeQuery();
            rs.next();
            balance = rs.getDouble("saldo_konta");
            money.setText(balance+"zł");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void placeBetClick(ActionEvent actionEvent){

    }

    @FXML
    private void searchClick(ActionEvent actionEvent){

    }
    @FXML
    private void topAccClick(ActionEvent actionEvent){

    }
    @FXML
    private void historyClick(ActionEvent actionEvent){

    }

    @FXML
    private void manageAccClick(ActionEvent actionEvent){

    }
}
