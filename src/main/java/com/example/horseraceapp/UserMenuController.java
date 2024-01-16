package com.example.horseraceapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    public TextField searchBarBet;
    @FXML
    public Label ref;
    @FXML
    ListView<BetCell> betList;
    @FXML
    Label nick;
    @FXML
    public Label money;
    Connection con;
    Statement stm;
    String username;
    Integer user_id;
    public Double balance;

    public void setNickAndBal(String username) throws ClassNotFoundException, SQLException{
        this.username = username;
        nick.setText(this.username);
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
        stm = con.createStatement();
        String sql = "SELECT id_uzytkownika, saldo_konta FROM uzytkownicy WHERE nazwa_uzytkownika = ?";
        PreparedStatement prp = con.prepareStatement(sql);
        prp.setString(1, this.username);
        ResultSet rs = prp.executeQuery();
        rs.next();
        user_id = rs.getInt("id_uzytkownika");
        balance = rs.getDouble("saldo_konta");
        updateBalance(balance);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        placeBetManage(false);
//        placeBet.setDisable(true);
//        initScreen();
    }

    @FXML
    public void placeBetClick() throws IOException, ClassNotFoundException, SQLException {

        placeBetManage(true);
        searchManage(false);
        initBetScreen();
    }

    private void initBetScreen(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM bet_table");
            betList.setCellFactory(param -> new BetCellFactory());
            while(rs.next()){
                Button button = new Button("Zatwierdź");
                BetCell betCell = new BetCell(rs.getInt("id_udzialu")+"!"+rs.getString("imie_konia")+" • "+rs.getString("imie_jezdzcy")+" "+rs.getString("nazwisko_jezdzcy")+" • "+rs.getString("opis_gonitwy")+" • "+rs.getString("data_wyscigu")+" "+rs.getString("czas")+" |"+rs.getString("kurs"),button);
                betCell.button.setOnMouseClicked(event -> {
                    double kwota = Double.parseDouble(betCell.textField.getText());
                    if(kwota <= balance && balance > 0.0){
                        String sql = "INSERT INTO kupony(id_udzialu, kwota, kurs, status_kuponu, id_uzytkownika) VALUES(?,?,?,NULL,?)";
                        try {
                            PreparedStatement prp = con.prepareStatement(sql);
                            prp.setInt(1, betCell.id_udzialu);
                            prp.setDouble(2, kwota);
                            prp.setDouble(3, betCell.kurs);
                            prp.setInt(4, user_id);
                            prp.executeUpdate();
                            balance -= kwota;
                            updateBalance(balance);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        betCell.outcome.setText(" brak środków!");
                    }
                });
                betList.getItems().add(betCell);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void placeBetManage(boolean vis){
        placeBet.setDisable(vis);
        betList.setVisible(vis);
        ref.setVisible(vis);
        searchBarBet.setVisible(vis);
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

    @FXML
    private void searchBarBetTyped(){
        System.out.println("ok");
    }
    private void updateBalance(Double newBalance){
        money.setText(newBalance + "zł");
    }
}
