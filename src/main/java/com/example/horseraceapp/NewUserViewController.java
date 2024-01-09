package com.example.horseraceapp;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class NewUserViewController {

    private Stage dialogStage;
    @FXML
    TextField newLogin;
    @FXML
    TextField newPassword;
    @FXML
    TextField repeatPassword;
    @FXML
    Label errorMsg;
    @FXML
    DatePicker birthday;

    Connection con;
    Statement stm;

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    @FXML
    public void onCreateAccountClick() throws SQLException, ClassNotFoundException, InterruptedException {
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace","administrator","admin");
        stm = con.createStatement();
        ResultSet rs = stm.executeQuery("SELECT nazwa_uzytkownika FROM uzytkownicy");
        String name = newLogin.getText();
        String pass = newPassword.getText();
        LocalDate bDay = birthday.getValue();
        Period age = Period.between(bDay, LocalDate.now());
        boolean sameLogin = false;
        while(rs.next()){
            if(Objects.equals(name,rs.getString("nazwa_uzytkownika"))){
                sameLogin = true;
                break;
            }
        }
        if(sameLogin)
            errorMsg.setText("Nazwa użytkownika zajęta!");
        else if(pass.isEmpty())
            errorMsg.setText("Pole Hasło nie może być puste!");
        else if(!Objects.equals(pass,repeatPassword.getText()))
            errorMsg.setText("Hasła się różnią!");
        else if(age.getYears()<18)
            errorMsg.setText("Użytkownik musi być pełnoletni!");
        else{
            errorMsg.setText("");
            String sql = "Insert INTO uzytkownicy(nazwa_uzytkownika, haslo_uzytkownika, data_urodzenia, saldo_konta,typ_konta) Values(?,?,?,?,?)";
            PreparedStatement prepStm = con.prepareStatement(sql);
            prepStm.setString(1,name);
            prepStm.setString(2,pass);
            prepStm.setDate(3,Date.valueOf(bDay));
            prepStm.setDouble(4,0.0);
            prepStm.setString(5,"Użytkownik");
            int rowsAffected = prepStm.executeUpdate();
            dialogStage.close();
        }
    }
}
