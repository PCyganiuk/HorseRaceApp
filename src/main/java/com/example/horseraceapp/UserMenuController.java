package com.example.horseraceapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    @FXML
    public Button placeBet;
    @FXML
    public Button search;
    @FXML
    public Button topAcc;
    @FXML
    public Button history;
    @FXML
    public Button manageAcc;
    @FXML
    public FlowPane fPane;
    @FXML
    public Label ref;
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

    Scene scene;

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
        placeBet.setDisable(true);
        initScreen();
    }

    @FXML
    private void placeBetClick(ActionEvent actionEvent){
        placeBetManage(true);
        searchManage(false);
        initScreen();
    }

    private void initScreen(){
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
                betList.getItems().add(rs.getString("imie_konia")+" • "+rs.getString("imie_jezdzcy")+" "+rs.getString("nazwisko_jezdzcy")+" • "+rs.getString("opis_gonitwy")+" • "+rs.getString("data_wyscigu")+" "+rs.getString("czas")+" |"+rs.getString("kurs"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void placeBetManage(boolean vis){
        placeBet.setDisable(vis);
        betList.setVisible(vis);
        ref.setVisible(vis);
        fPane.setVisible(vis);
        if(!vis){
            betList.getItems().removeAll();
        }
    }

    @FXML
    private void searchClick(ActionEvent actionEvent){
        searchManage(true);
        placeBetManage(false);
    }
    private void searchManage(boolean vis){
        search.setDisable(vis);
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
