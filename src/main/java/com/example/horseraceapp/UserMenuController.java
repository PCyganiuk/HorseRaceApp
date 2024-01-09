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
    TableView<Bet> betTable;
    @FXML
    TableColumn<Bet,String> horseNameCol;
    @FXML
    TableColumn<Bet,String> riderCol;
    @FXML
    TableColumn<Bet,String> raceCol;
    @FXML
    TableColumn<Bet,Double> rateCol;
    @FXML
    TableColumn<Bet,String> dateCol;
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
        horseNameCol.setCellValueFactory(new PropertyValueFactory<>("Koń"));
        riderCol.setCellValueFactory(new PropertyValueFactory<>("Jeźdźca"));
        raceCol.setCellValueFactory(new PropertyValueFactory<>("Gonitwa"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("Kurs"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Data wyścigu"));
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
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
