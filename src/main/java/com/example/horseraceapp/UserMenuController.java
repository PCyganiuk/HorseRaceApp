package com.example.horseraceapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

import static java.sql.DriverManager.getConnection;
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
    public Button startRace;
    @FXML
    public TextField searchBarBet;
    @FXML
    public Label ref;
    @FXML
    ListView<BetCell> betList;
    public TextField searchBarSearch = new TextField();
    TableView<SearchHorse> searchTable = new TableView<>();
    @FXML
    Label nick;
    @FXML
    public Label money;
    ComboBox<String> startRdy = new ComboBox<>();
    Button start = new Button("Rozpocznij");
    Connection con;
    Statement stm;
    String username;
    Integer user_id;
    public Double balance;
    private final DecimalFormat df = new DecimalFormat("0.00");

    private final TextFormatter<String> numericFormat = new TextFormatter<>(change -> {
        String newText = change.getControlNewText();
        if (newText.matches("[0-9]*\\.?[0-9]*")) {
            return change;  // Allow the change
        }
        else {
            return null;    // Reject the change
        }
    });

    public void setNickAndBal(String username) throws ClassNotFoundException, SQLException{
        this.username = username;
        nick.setText(this.username);
        Class.forName("org.postgresql.Driver");
        con = getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
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
    }

    @FXML
    public void placeBetClick() {
        placeBetManage(true);
        searchManage(false);
        topAccManage(false);
        historyManage(false);
        startRaceManage(false);
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
        historyManage(false);
        placeBetManage(false);
        topAccManage(false);
        startRaceManage(false);
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
    TextField amount = new TextField();
    Button confirm = new Button("Zatwierdź");
    Label top = new Label("Doładuj Konto");
    @FXML
    private void topAccClick(){
        searchManage(false);
        historyManage(false);
        placeBetManage(false);
        startRaceManage(false);
        topAccManage(true);
        if(!anchorPane.getChildren().contains(confirm)) {
            AnchorPane.setTopAnchor(top, 130.0);
            AnchorPane.setLeftAnchor(top, 284.0);
            AnchorPane.setRightAnchor(top, 167.0);
            top.setAlignment(Pos.CENTER);
            AnchorPane.setTopAnchor(confirm, 200.0);
            AnchorPane.setLeftAnchor(confirm, 320.0);
            AnchorPane.setRightAnchor(confirm, 204.0);
            confirm.setPrefWidth(50.0);
            confirm.setOnMouseClicked(event -> {
                if(!amount.getText().isEmpty()) {
                    balance += Double.parseDouble(amount.getText());
                    updateBalance(balance);
                }
            });

            AnchorPane.setTopAnchor(amount, 163.0);
            AnchorPane.setLeftAnchor(amount, 284.0);
            AnchorPane.setRightAnchor(amount, 167.0);
            amount.setPromptText("Wartość doładowania");
            amount.setTextFormatter(numericFormat);
            anchorPane.getChildren().add(amount);
            anchorPane.getChildren().add(top);
            anchorPane.getChildren().add(confirm);
        }
    }
    private void topAccManage(boolean vis){
        topAcc.setDisable(vis);
        confirm.setVisible(vis);
        amount.setVisible(vis);
        top.setVisible(vis);
        amount.setText("");
    }
    ListView<String> couponHistory = new ListView<>();
    @FXML
    private void historyClick(ActionEvent actionEvent) throws SQLException {
        historyManage(true);
        startRaceManage(false);
        searchManage(false);
        placeBetManage(false);
        topAccManage(false);
        if(!anchorPane.getChildren().contains(couponHistory)) {
            AnchorPane.setTopAnchor(couponHistory, 112.0);
            AnchorPane.setBottomAnchor(couponHistory, 6.0);
            AnchorPane.setLeftAnchor(couponHistory, 125.0);
            AnchorPane.setRightAnchor(couponHistory, 11.0);
            anchorPane.getChildren().add(couponHistory);
        }
        ObservableList<String> coupList = FXCollections.observableArrayList();
        con = getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
        String sql = "SELECT k.imie_konia,g.opis_gonitwy ,kwota,kupony.kurs,status_kuponu FROM kupony INNER JOIN public.udzial_w_gonitwach uwg on kupony.id_udzialu = uwg.id_udzialu INNER JOIN public.konie k on uwg.id_konia = k.id_konia INNER JOIN public.gonitwy g on uwg.id_gonitwy = g.id_gonitwy WHERE id_uzytkownika = ?";
        PreparedStatement prp = con.prepareStatement(sql);
        prp.setInt(1,user_id);
        ResultSet rs = prp.executeQuery();
        while(rs.next()){
            if(rs.getBoolean("status_kuponu")) {
                double suma = rs.getDouble("kurs")*rs.getDouble("kwota");
                String roundedNumber = df.format(suma);
                coupList.add(rs.getString("imie_konia")+"•"+rs.getString("opis_gonitwy")+"• +" +roundedNumber+"zł");
            }
            else if(!rs.getBoolean("status_kuponu")){

                coupList.add(rs.getString("imie_konia")+"•"+rs.getString("opis_gonitwy")+"• -"+df.format(rs.getDouble("kwota"))+"zł");
            }
        }
        couponHistory.setItems(coupList);
    }
    private void historyManage(boolean vis){
        history.setDisable(vis);
        couponHistory.setVisible(vis);
    }
    Label startInfo = new Label("Wybierz gonitwę do rozegrania");
    Label raceOutcome = new Label("");

    @FXML
    private void startRaceClick(ActionEvent actionEvent){
        raceOutcome.setText("");
        startRaceManage(true);
        searchManage(false);
        historyManage(false);
        placeBetManage(false);
        topAccManage(false);
        if(!anchorPane.getChildren().contains(startRdy)) {
            AnchorPane.setTopAnchor(startInfo, 105.0);
            AnchorPane.setLeftAnchor(startInfo, 229.0);
            AnchorPane.setRightAnchor(startInfo, 121.0);

            AnchorPane.setTopAnchor(raceOutcome, 205.0);
            AnchorPane.setLeftAnchor(raceOutcome, 229.0);
            AnchorPane.setRightAnchor(raceOutcome, 121.0);

            AnchorPane.setTopAnchor(startRdy, 135.0);
            AnchorPane.setLeftAnchor(startRdy, 229.0);
            AnchorPane.setRightAnchor(startRdy, 121.0);

            AnchorPane.setTopAnchor(start, 165.0);
            AnchorPane.setLeftAnchor(start, 308.0);
            AnchorPane.setRightAnchor(start, 200.0);
            startInfo.setAlignment(Pos.CENTER);
            raceOutcome.setAlignment(Pos.CENTER);

            anchorPane.getChildren().add(startInfo);
            anchorPane.getChildren().add(startRdy);
            anchorPane.getChildren().add(start);
            anchorPane.getChildren().add(raceOutcome);
        }
        ObservableList<String> race = FXCollections.observableArrayList();
        ArrayList<Integer> idx = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            String sql = "SELECT DISTINCT gonitwy.id_gonitwy, gonitwy.opis_gonitwy, gonitwy.data_wyscigu, gonitwy.czas FROM gonitwy INNER JOIN public.udzial_w_gonitwach uwg on gonitwy.id_gonitwy = uwg.id_gonitwy WHERE uwg.wynik_konia IS NULL";
            PreparedStatement prp = con.prepareStatement(sql);
            ResultSet rs = prp.executeQuery();
            while(rs.next()){
                race.add(rs.getString("opis_gonitwy")+" "+rs.getString("data_wyscigu")+" "+rs.getString("czas"));
                idx.add(rs.getInt("id_gonitwy"));
            }
            startRdy.setItems(race);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        start.setOnMouseClicked(mouseEvent -> {
            try {
                int i = startRdy.getSelectionModel().getSelectedIndex();
                con = getConnection("jdbc:postgresql://localhost:5432/HorseRace", "administrator", "admin");
                String sql = "SELECT imie_konia, id_udzialu, kurs FROM udzial_w_gonitwach INNER JOIN public.konie k on udzial_w_gonitwach.id_konia = k.id_konia WHERE id_gonitwy = ?;";
                PreparedStatement prp = con.prepareStatement(sql);
                prp.setInt(1,idx.get(i));
                ResultSet rs = prp.executeQuery();
                ArrayList<Integer> udzial = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                while(rs.next()){
                    names.add(rs.getString("imie_konia"));
                    udzial.add(rs.getInt("id_udzialu"));
                }
                Collections.shuffle(udzial);
                raceOutcome.setText(names.get(0)+" - "+ udzial.get(0)+"\n"+names.get(1)+" - "+ udzial.get(1)+"\n"+names.get(2)+" - "+ udzial.get(2)+"\n"+names.get(3)+" - "+ udzial.get(3)+"\n"+names.get(4)+" - "+ udzial.get(4));
                int winId = udzial.get(0);
                for(int j = 0; j< udzial.size();j++){
                    String sql2 = "UPDATE udzial_w_gonitwach SET wynik_konia = ? WHERE id_udzialu = ?;";
                    PreparedStatement prp2 = con.prepareStatement(sql2);
                    prp2.setInt(1,j+1);
                    prp2.setInt(2,udzial.get(j));
                    prp2.executeUpdate();
                    String sql3 = "UPDATE kupony SET status_kuponu = ? WHERE id_udzialu = ?";
                    PreparedStatement prp3 = con.prepareStatement(sql3);
                    if(j==0)
                        prp3.setBoolean(1,true);
                    else
                        prp3.setBoolean(1,false);
                    prp3.setInt(2,udzial.get(j));
                    prp3.executeUpdate();
                    if(j == 0) {
                        String sql4 = "SELECT id_uzytkownika, kwota, kurs FROM kupony WHERE id_udzialu = ?";
                        PreparedStatement prp4 = con.prepareStatement(sql4);
                        prp4.setInt(1, udzial.get(j));
                        ResultSet rs2 = prp4.executeQuery();
                        while(rs2.next()){
                            String sql5 = "UPDATE uzytkownicy SET saldo_konta = saldo_konta + ? WHERE id_uzytkownika = ?";
                            PreparedStatement prp5 = con.prepareStatement(sql5);
                            prp5.setDouble(1,rs2.getDouble("kwota")* rs2.getDouble("kurs"));
                            prp5.setInt(2,rs2.getInt("id_uzytkownika"));
                            prp5.executeUpdate();
                            if(rs2.getInt("id_uzytkownika")== user_id) {
                                balance += rs2.getDouble("kwota") * rs2.getDouble("kurs");
                                money.setText(balance + "zł");
                                System.out.println("wygrałeś");
                            }
                        }
                    }
                }
                startRdy.getItems().remove(i);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void startRaceManage(boolean vis){
        startRace.setDisable(vis);
        startRdy.setVisible(vis);
        start.setVisible(vis);
        startInfo.setVisible(vis);
        raceOutcome.setVisible(vis);
    }

    @FXML
    private void searchBarBetTyped(){
        betList.getItems().clear();
        initBetScreen(searchBarBet.getText());
    }
    private void updateBalance(Double newBalance){
        money.setText(newBalance + "zł");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            String sql = "UPDATE uzytkownicy SET saldo_konta = ? WHERE id_uzytkownika = ?";
            PreparedStatement prp = con.prepareStatement(sql);
            prp.setDouble(1,newBalance);
            prp.setInt(2,user_id);
            prp.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void initBetScreen(String par){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            con = getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            Statement stm = con.createStatement();
            ResultSet rs;
            if(par.isEmpty())
                rs = stm.executeQuery("SELECT * FROM bet_table");
            else{
                String sql = "SELECT * FROM bet_table WHERE " +
                        "UPPER(imie_konia) LIKE UPPER(?) OR UPPER(imie_jezdzcy || ' ' || bet_table.nazwisko_jezdzcy) LIKE UPPER(?) OR UPPER(opis_gonitwy) LIKE UPPER(?)";
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
                            button.setText("✓");
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
            con = getConnection("jdbc:postgresql://localhost:5432/HorseRace", "uzytkownik", "user123");
            Statement stm = con.createStatement();
            ResultSet rs;
            if(par.isEmpty())
                rs = stm.executeQuery("SELECT * FROM horse_statistics");
            else{
                String sql = "SELECT * FROM horse_statistics WHERE UPPER(imie_konia) LIKE UPPER(?)";
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
