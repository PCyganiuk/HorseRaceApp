package com.example.horseraceapp;

import dataAccess.Horse;
import dataAccess.Kupon;
import dataAccess.StatystykiKonia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminMenuController {
    @FXML
    private TextField imie_konia;
    @FXML
    private TextField rasa_konia;
    @FXML
    private TextField plec_konia;
    @FXML
    private DatePicker data_urodzenia;

    @FXML
    Label adminNick;
    @FXML
    public Label money;
    Connection con;
    Statement stm;
    String username;
    Integer admin_id;
    public Double adminBalance;
    @FXML
    private TableView<StatystykiKonia> tableHorseStats;
    @FXML
    private TextField searchField;
    @FXML
    private VBox statsVBox;
    @FXML
    private VBox addvBox;
    @FXML
    private TableView<Kupon> tableViewWinKupony;
    @FXML
    private TableColumn<Kupon, Integer> idKuponuColumn;
    @FXML
    private TableColumn<Kupon, Integer> idUdzialuColumn;
    @FXML
    private TableColumn<Kupon, Double> kwotaColumn;
    @FXML
    private TableColumn<Kupon, Double> kursColumn;
    @FXML
    private TableColumn<Kupon, Void> actionColumn;



    public void setAdminNickAndBal(String username) throws ClassNotFoundException, SQLException {
        this.username = username;
        adminNick.setText(this.username);
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
        stm = con.createStatement();

        String sql = "SELECT id_uzytkownika, saldo_konta FROM uzytkownicy WHERE nazwa_uzytkownika = ? AND typ_konta = 'Administrator'";
        PreparedStatement prp = con.prepareStatement(sql);
        prp.setString(1, this.username);
        ResultSet rs = prp.executeQuery();
        rs.next();

        admin_id = rs.getInt("id_uzytkownika");
        adminBalance = rs.getDouble("saldo_konta");
        updateAdminBalance(adminBalance);
    }

    private void updateAdminBalance(Double newBalance) {
        money.setText(newBalance + "zł");
    }
    @FXML
    public void showAddHorseInterface(ActionEvent event) {
        if (statsVBox != null) {
            statsVBox.setVisible(false);
        }
        if (tableViewWinKupony != null) {
            tableViewWinKupony.setVisible(false);
        }

        addvBox.setVisible(true);
    }

    public void handleAddRemoveHorse(ActionEvent actionEvent) {
        statsVBox.setVisible(false);
        tableViewWinKupony.setVisible(false);
        tableViewWinKupony.setVisible(false);
        addvBox.setVisible(true);

        String imieKonia = imie_konia.getText();
        String rasaKonia = rasa_konia.getText();
        String plecKonia = plec_konia.getText();
        LocalDate dataWyscigu = data_urodzenia.getValue();



        String sql = "INSERT INTO konie (imie_konia, data_urodzenia, rasa_konia, plec_konia) VALUES (?, ?, ?, ?);";
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
                 PreparedStatement prp = con.prepareStatement(sql)) {

                prp.setString(1, imieKonia);
                prp.setString(3, rasaKonia);
                prp.setString(4, plecKonia);
                if (dataWyscigu != null) {

                    prp.setDate(2,   Date.valueOf(dataWyscigu));
                } else {
                    prp.setNull(2, Types.DATE);
                }

                int affectedRows = prp.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Koń został pomyślnie dodany do bazy danych.");
                } else {
                    System.out.println("Nie udało się dodać konia do bazy danych.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Nie udało się dodać konia do bazy danych: " + e.getMessage());
        }
    }

    @FXML
    private void handleSchedule(ActionEvent event) {
        String sqlKonie = "SELECT id_konia FROM konie;";
        String sqlJezdzcy = "SELECT id_jezdzcy FROM jezdzcy;";
        String sqlInsertUdzial = "INSERT INTO udzial_w_gonitwach (id_konia, id_gonitwy, id_jezdzcy, kurs, wynik_konia) VALUES (?, ?, ?, ?, ?);";

        List<Integer> konieIds = new ArrayList<>();
        List<Integer> jezdzcyIds = new ArrayList<>();
        Random random = new Random();

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
             Statement stmt = con.createStatement()) {
            // Pobieranie ID koni
            ResultSet rsKonie = stmt.executeQuery(sqlKonie);
            while (rsKonie.next()) {
                konieIds.add(rsKonie.getInt("id_konia"));
            }

            // Pobieranie ID jeźdźców
            ResultSet rsJezdzcy = stmt.executeQuery(sqlJezdzcy);
            while (rsJezdzcy.next()) {
                jezdzcyIds.add(rsJezdzcy.getInt("id_jezdzcy"));
            }

            // Przydzielanie jeźdźców do koni i tworzenie udziałów w gonitwach
            for (Integer konId : konieIds) {
                int jezdziecId = jezdzcyIds.get(random.nextInt(jezdzcyIds.size()));

//                try (PreparedStatement prpStmt = con.prepareStatement(sqlInsertUdzial)) {
//                    prpStmt.setInt(1, konId);
//                    prpStmt.setInt(2, // jakie id gonitwy?);
//                    prpStmt.setInt(3, jezdziecId);
//                    prpStmt.setDouble(4, /* Kurs losowy? */ 1.5 + (4.5 - 1.5) * random.nextDouble());
//                    prpStmt.setInt(5, /* Wynik domyslny jako 0? */ 0);
//                    prpStmt.executeUpdate();
//                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas układania harmonogramu: " + e.getMessage());
        }
    }



    @FXML
    private void handleHorseStats(ActionEvent event) {
        statsVBox.setVisible(true);
        addvBox.setVisible(false);
        tableViewWinKupony.setVisible(false);
        ObservableList<StatystykiKonia> horseStats = FXCollections.observableArrayList();

        String sql = "SELECT u.id_udzialu, k.imie_konia, u.id_gonitwy, u.id_jezdzcy, u.kurs, u.wynik_konia " +
                "FROM udzial_w_gonitwach u JOIN konie k ON u.id_konia = k.id_konia;";

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Horse horse = new Horse();
                horse.setImieKonia(rs.getString("imie_konia"));

                StatystykiKonia statystyki = new StatystykiKonia();
                statystyki.setKon(horse);
                statystyki.setIdUdzialu(rs.getInt("id_udzialu"));
                statystyki.setIdGonitwy(rs.getInt("id_gonitwy"));
                statystyki.setIdJezdzcy(rs.getInt("id_jezdzcy"));
                statystyki.setKurs(rs.getDouble("kurs"));
                statystyki.setWynikKonia(rs.getInt("wynik_konia"));

                horseStats.add(statystyki);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas pobierania statystyk koni: " + e.getMessage());
        }

        tableHorseStats.setItems(horseStats);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrujStatystykiKoni(newValue, horseStats);
        });
    }
    private void filtrujStatystykiKoni(String filter, ObservableList<StatystykiKonia> horseStats) {
        if (filter == null || filter.isEmpty()) {
            tableHorseStats.setItems(horseStats);
        } else {
            ObservableList<StatystykiKonia> filteredData = FXCollections.observableArrayList();
            for (StatystykiKonia stats : horseStats) {
                if (stats.getKon().getImieKonia().toLowerCase().contains(filter.toLowerCase())) {
                    filteredData.add(stats);
                }
            }
            tableHorseStats.setItems(filteredData);
        }
    }

    @FXML
    private void handleConfirmWin(ActionEvent event) {
        tableViewWinKupony.setVisible(true);


        ObservableList<Kupon> kupony = FXCollections.observableArrayList();

        String sql = "SELECT * FROM kupony WHERE status_kuponu = true;";

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Kupon kupon = new Kupon();
                kupon.setIdKuponu( rs.getInt("id_kuponu"));
                kupon.setIdUdzialu( rs.getInt("id_udziału"));
                kupon.setKwota(rs.getDouble("kwota"));
                kupon.setKurs(rs.getDouble("kurs"));
                kupon.setStatusKuponu(rs.getBoolean("status_kuponu"));
                kupon.setIdUzytkownika(rs.getInt("id_uzytkownika"));

                kupony.add(kupon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas pobierania wygranych kuponów: " + e.getMessage());
        }

        tableViewWinKupony.setItems(kupony);
    }
    @FXML
    public void initialize() {

        idKuponuColumn.setCellValueFactory(new PropertyValueFactory<>("idKuponu"));
        idUdzialuColumn.setCellValueFactory(new PropertyValueFactory<>("idUdzialu"));
        kwotaColumn.setCellValueFactory(new PropertyValueFactory<>("kwota"));
        kursColumn.setCellValueFactory(new PropertyValueFactory<>("kurs"));

        actionColumn.setCellFactory(param -> new TableCell<Kupon, Void>() {
            private final Button btn = new Button("Potwierdź");

            {
                btn.setOnAction(event -> {
                    Kupon kupon = getTableView().getItems().get(getIndex());
                    potwierdzKupon(kupon);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        loadKupony();
    }

    private void loadKupony() {
        ObservableList<Kupon> kupony = FXCollections.observableArrayList();
        String sql = "SELECT id_kuponu, id_udzialu, kwota, kurs, status_kuponu, id_uzytkownika FROM kupony;";

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
             PreparedStatement stmt = con.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Kupon kupon = new Kupon();
                kupon.setIdKuponu( rs.getInt("id_kuponu"));
                kupon.setIdUdzialu( rs.getInt("id_udziału"));
                kupon.setKwota(rs.getDouble("kwota"));
                kupon.setKurs(rs.getDouble("kurs"));
                kupon.setStatusKuponu(rs.getBoolean("status_kuponu"));
                kupon.setIdUzytkownika(rs.getInt("id_uzytkownika"));
                kupony.add(kupon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas ładowania kuponów: " + e.getMessage());
        }

        tableViewWinKupony.setItems(kupony);
    }


    private void potwierdzKupon(Kupon kupon) {
        String sql = "UPDATE kupony SET status_kuponu = false WHERE id_kuponu = ?;";

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, kupon.getIdKuponu());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Kupon potwierdzony pomyślnie.");
            } else {
                System.out.println("Nie udało się potwierdzić kuponu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas potwierdzania kuponu: " + e.getMessage());
        }
    }


    public void handleControlOdds(ActionEvent actionEvent) {

    }
}
