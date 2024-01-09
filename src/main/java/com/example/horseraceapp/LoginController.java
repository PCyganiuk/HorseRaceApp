package com.example.horseraceapp;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Label bladText;
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<String> type;
    Connection con;
    Statement stm;
    String nazwa;

    @FXML
    protected void onLoginClick(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace","uzytkownik","user123");
        stm = con.createStatement();
        ResultSet rs = stm.executeQuery("SELECT nazwa_uzytkownika, haslo_uzytkownika, typ_konta FROM uzytkownicy;");
        nazwa = login.getText();
        String haslo = password.getText();
        String sel = type.getSelectionModel().getSelectedItem();
        boolean bad = true;
        while(rs.next()){
            if(Objects.equals(nazwa, rs.getString("nazwa_uzytkownika")) && Objects.equals(haslo,rs.getString("haslo_uzytkownika")) && sel.equals(rs.getString("typ_konta"))){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("user-menu.fxml"));
                URL location = loader.getLocation();
                System.out.println(location);
                Parent root = loader.load();
                UserMenuController userMenuController = loader.getController();
                userMenuController.setNickandBal(nazwa);
                //Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                System.out.println(nazwa);
                stage.show();
                System.out.println("login success");
                bad = false;
                break;
            }
        }
        bladText.setVisible(bad);
    }
    @FXML
    public void newUserClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("new-user-view.fxml"));
        Parent root = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Rejestracja Konta");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        NewUserViewController newUserViewController = loader.getController();
        newUserViewController.setDialogStage(dialogStage);

        dialogStage.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.setItems(FXCollections.observableArrayList("Administrator", "Użytkownik", "Menadżer"));
    }
}