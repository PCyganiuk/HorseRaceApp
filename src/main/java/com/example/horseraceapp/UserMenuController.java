package com.example.horseraceapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;

public class UserMenuController implements Initializable {
    @FXML
    public Button placeBet;
    @FXML
    AnchorPane anchorPane;
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
    public TextField searchBarSearch = new TextField();
    TableView<SearchHorse> searchTable = new TableView<SearchHorse>();
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
    public void placeBetClick() {
        placeBetManage(true);
        searchManage(false);
        topAccManage(false);
        betList.getItems().clear();
        initBetScreen("");
    }
    
    private void placeBetManage(boolean vis){
        placeBet.setDisable(vis);
        betList.setVisible(vis);
        ref.setVisible(vis);
        searchBarBet.setText("");
        searchBarBet.setVisible(vis);
        if(!vis){
            betList.getItems().removeAll();
        }
    }

    @FXML
    private void searchClick(){
        searchManage(true);
        placeBetManage(false);
        topAccManage(false);
        searchTable.getColumns().clear();
        if(!anchorPane.getChildren().contains(searchTable)) {
            searchTable.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
            AnchorPane.setTopAnchor(searchBarSearch, 85.0);
            AnchorPane.setLeftAnchor(searchBarSearch, 125.0);
            AnchorPane.setRightAnchor(searchBarSearch, 11.0);
            searchBarSearch.setPromptText("Wyszukaj konia");
            searchBarSearch.setOnKeyTyped(event -> {
                searchTable.getColumns().clear();
                initSearchScreen(searchBarSearch.getText().replace(" ",""));
            });
            AnchorPane.setTopAnchor(searchTable, 112.0);
            AnchorPane.setBottomAnchor(searchTable, 6.0);
            AnchorPane.setLeftAnchor(searchTable, 125.0);
            AnchorPane.setRightAnchor(searchTable, 11.0);
            anchorPane.getChildren().add(searchBarSearch);
            anchorPane.getChildren().add(searchTable);
        }
        initSearchScreen("");
    }
    private void searchManage(boolean vis){
        search.setDisable(vis);
        searchTable.setVisible(vis);
        searchBarSearch.setText("");
        searchBarSearch.setVisible(vis);
    }
    @FXML
    private void topAccClick(ActionEvent actionEvent){
        searchManage(false);
        placeBetManage(false);
        topAccManage(true);
    }
    private void topAccManage(boolean vis){
        topAcc.setDisable(vis);

    }
    @FXML
    private void historyClick(ActionEvent actionEvent){

    }

    @FXML
    private void manageAccClick(ActionEvent actionEvent){

    }

    @FXML
    private void searchBarBetTyped(){
        betList.getItems().clear();
        initBetScreen(searchBarBet.getText().replace(" ",""));
    }
    private void updateBalance(Double newBalance){
        money.setText(newBalance + "zł");
    }

    private void initBetScreen(String par){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            Statement stm = con.createStatement();
            ResultSet rs;
            if(par.isEmpty())
                rs = stm.executeQuery("SELECT * FROM bet_table");
            else{
                String sql = "SELECT * FROM bet_table WHERE " +
                        "UPPER(imie_konia) LIKE UPPER(?) OR UPPER(imie_jezdzcy || bet_table.nazwisko_jezdzcy) LIKE UPPER(?) OR UPPER(opis_gonitwy) LIKE UPPER(?)";
                PreparedStatement prp = con.prepareStatement(sql);
                prp.setString(1,"%"+par+"%");
                prp.setString(2,"%"+par+"%");
                prp.setString(3,"%"+par+"%");
                rs = prp.executeQuery();
            }

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
    private void initSearchScreen(String par){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            Statement stm = con.createStatement();
            ResultSet rs;
            if(par.isEmpty())
                rs = stm.executeQuery("SELECT * FROM horse_statisctics");
            else{
                String sql = "SELECT * FROM horse_statisctics WHERE UPPER(imie_konia) LIKE UPPER(?)";
                PreparedStatement prp = con.prepareStatement(sql);
                prp.setString(1,"%"+par+"%");
                rs = prp.executeQuery();
            }
            ObservableList<SearchHorse> searchHorses = FXCollections.observableArrayList();
            while(rs.next()) {
                searchHorses.add(new SearchHorse(rs.getString(2),rs.getInt(3), rs.getInt(4),rs.getInt(5),rs.getDouble(6),rs.getInt(7)));
            }
            TableColumn<SearchHorse, String> nameCol = new TableColumn<>("Imię Konia");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<SearchHorse, Integer> compRacesCol = new TableColumn<>("Ukończone Gonitwy");
            compRacesCol.setCellValueFactory(new PropertyValueFactory<>("compRaces"));

            TableColumn<SearchHorse, Integer> winsCol = new TableColumn<>("Wygrane Gonitwy");
            winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));

            TableColumn<SearchHorse, Integer> losesCol = new TableColumn<>("Przegrane Gonitwy");
            losesCol.setCellValueFactory(new PropertyValueFactory<>("loses"));

            TableColumn<SearchHorse, Integer> ageColumn = new TableColumn<>("Wiek Konia");
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

            TableColumn<SearchHorse, Double> avgOddsColumn = new TableColumn<>("Średni Kurs w Gonitwach");
            avgOddsColumn.setCellValueFactory(new PropertyValueFactory<>("avgOdds"));

            searchTable.setItems(searchHorses);
            searchTable.getColumns().addAll(nameCol,compRacesCol,winsCol,losesCol,avgOddsColumn,ageColumn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
