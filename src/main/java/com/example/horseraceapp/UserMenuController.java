package com.example.horseraceapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    @FXML
    ListView<String> betList;
    @FXML
    Label nick;
    @FXML
    Label money;
    Connection con;
    Statement stm;
    String username;
    Double balance;

    public void setNickAndBal(String username) throws ClassNotFoundException, SQLException{
        this.username = username;
        nick.setText(this.username);
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
        stm = con.createStatement();
        String sql = "SELECT saldo_konta FROM uzytkownicy WHERE nazwa_uzytkownika = ?";
        PreparedStatement prp = con.prepareStatement(sql);
        prp.setString(1, this.username);
        ResultSet rs = prp.executeQuery();
        rs.next();
        balance = rs.getDouble("saldo_konta");
        money.setText(balance + "zł");

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM bet_table");
            betList.setCellFactory(param -> new BetCell());
            while(rs.next()){
                betList.getItems().add(rs.getString(1)+" • "+rs.getString(2)+" "+rs.getString(3)+" • "+ rs.getString(4)+" • "+rs.getString(6)+" "+rs.getString(7)+" • "+rs.getString(5));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
