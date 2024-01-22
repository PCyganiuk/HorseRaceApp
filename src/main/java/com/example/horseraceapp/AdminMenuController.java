package com.example.horseraceapp;

import dataAccess.Harmonogram;
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
import java.time.LocalTime;
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
    private TableView<Horse> tableHorses;
    @FXML
    private TableView<Harmonogram> tableHarmonogram;


    @FXML
    private TableColumn<Harmonogram, Integer> idGonitwyColumnHarm;
    @FXML
    private TableColumn<Harmonogram, Integer> sezonColumnHarm;
    @FXML
    private TableColumn<Harmonogram, String> opisGonitwyColumnHarm;
    @FXML
    private TableColumn<Harmonogram, LocalDate> dataWysciguColumnHarm;
    @FXML
    private TableColumn<Harmonogram, LocalTime> czasColumnHarm;


    @FXML
    private TableColumn<Horse, Integer> idColumn;

    @FXML
    private TableColumn<Horse, String> imieKoniaColumn;

    @FXML
    private TableColumn<Horse, LocalDate> dataUrodzeniaColumn;

    @FXML
    private TableColumn<Horse, String> rasaKoniaColumn;

    @FXML
    private TableColumn<Horse, String> plecKoniaColumn;


    @FXML
    private TableView<StatystykiKonia> tableHorseStats;

    @FXML
    private TableColumn<Kupon, Integer> idUdzialuKuponColumn;
    @FXML
    private TableColumn<StatystykiKonia, Integer> idKoniaColumn;
    @FXML
    private TableColumn<StatystykiKonia, Integer> idKoniaColumnn;
    @FXML
    private TableColumn<Kupon, Integer> idKuponuColumn;
    @FXML
    private TableColumn<Kupon, Double> kwotaColumn;

    @FXML
    private TableColumn<StatystykiKonia, Integer> idGonitwyColumn;
    @FXML
    private TableColumn<StatystykiKonia, Integer> idJezdzcyColumn;
    @FXML
    private TableColumn<StatystykiKonia, Double> kursColumn;
    @FXML
    private TableColumn<StatystykiKonia, Double> idUdzialuColumn;

    @FXML
    private TableColumn<Kupon, Double> kursWinKuponyColumn;
    @FXML
    private TableColumn<StatystykiKonia, Double> wynikKoniaColumn;
    @FXML
    private TableColumn<Kupon, Boolean> statusKuponu;
    @FXML
    private TableColumn<StatystykiKonia, Integer> idUzytkownika;


    @FXML
    private TextField searchField;
    @FXML
    private VBox statsVBox;
    @FXML
    private VBox winKuponyVBox;
    @FXML
    private VBox removeHorseVBox;
    @FXML
    private VBox addvBox;
    @FXML
    private TableView<Kupon> tableViewWinKupony;

    @FXML
    private TableColumn<Kupon, Integer> idUzytkownikaKupon;


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

    @FXML
    private void loadHorses() {
        removeHorseVBox.setVisible(true);
        addvBox.setVisible(false);
        statsVBox.setVisible(false);
        winKuponyVBox.setVisible(false);
        ObservableList<Horse> horsesList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM konie;";

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Horse horse = new Horse();
                horse.setIdKonia(rs.getInt("id_konia"));
                horse.setImieKonia(rs.getString("imie_konia"));
                horse.setRasaKonia(rs.getString("rasa_konia"));
                horse.setPlecKonia(rs.getString("plec_konia"));
                Date dbDate = rs.getDate("data_urodzenia");
                if (dbDate != null) {
                    horse.setDataUrodzenia(dbDate.toLocalDate());
                }
                horsesList.add(horse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas ładowania koni: " + e.getMessage());
        }

        tableHorses.setItems(horsesList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrujKonie(newValue, horsesList);
        });

    }

    private int findAvailableHorseId() throws SQLException {
        String sql = "SELECT id_konia FROM konie WHERE id_konia NOT IN (SELECT DISTINCT id_konia FROM udzial_w_gonitwach);";

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("id_konia");
            } else {
                throw new SQLException("Nie znaleziono dostępnego id_konia.");
            }
        }
    }

    private void updateParticipationsBeforeHorseDeletion(int horseIdToDelete) {
        try {
            int newHorseId = findAvailableHorseId();
            String sqlUpdateParticipations = "UPDATE udzial_w_gonitwach SET id_konia = ? WHERE id_konia = ?;";

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
                 PreparedStatement pstmt = con.prepareStatement(sqlUpdateParticipations)) {

                pstmt.setInt(1, newHorseId);
                pstmt.setInt(2, horseIdToDelete);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Udziały w gonitwach zostały zaktualizowane.");
                } else {
                    System.out.println("Nie znaleziono udziałów do zaktualizowania.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Błąd podczas aktualizacji udziałów w gonitwach: " + e.getMessage());
        }
    }

    @FXML
    private void removeHorse() {

        // Pobierz aktualnie zaznaczonego konia z TableView.
        Horse selectedHorse = tableHorses.getSelectionModel().getSelectedItem();
        if (selectedHorse != null) {
            updateParticipationsBeforeHorseDeletion(selectedHorse.getIdKonia());
            String sql = "DELETE FROM konie WHERE id_konia = ?;";

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
                 PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, selectedHorse.getIdKonia()); // Załóżmy, że getId() zwraca id_konia konia z bazy danych.

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    // Usuń konia z widoku po pomyślnym usunięciu z bazy danych.
                    tableHorses.getItems().remove(selectedHorse);
                    System.out.println("Koń został usunięty z bazy danych.");
                } else {
                    System.out.println("Nie udało się usunąć konia z bazy danych.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Błąd podczas usuwania konia: " + e.getMessage());
            }
        } else {
            System.out.println("Nie wybrano konia do usunięcia.");
        }
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
            tableViewWinKupony.setVisible(true);
        }

        addvBox.setVisible(true);
    }

    public void handleAddHorse(ActionEvent actionEvent) {
        addvBox.setVisible(true);
        removeHorseVBox.setVisible(false);
        statsVBox.setVisible(false);
        winKuponyVBox.setVisible(false);
        addvBox.setVisible(false);

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

                    prp.setDate(2, Date.valueOf(dataWyscigu));
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
        String sqlInsertGonitwa = "INSERT INTO gonitwy (sezon, opis_gonitwy, data_wyscigu, czas) VALUES (?, ?, ?, ?);";
        String sqlInsertUdzial = "INSERT INTO udzial_w_gonitwach (id_konia, id_gonitwy, id_jezdzcy, kurs, wynik_konia) VALUES (?, ?, ?, ?, ?);";

        List<Integer> konieIds = new ArrayList<>();
        List<Integer> jezdzcyIds = new ArrayList<>();
        Random random = new Random();

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin")) {
            // Wyłączenie auto-commita
            con.setAutoCommit(false);

            try (Statement stmtKonie = con.createStatement();
                 Statement stmtJezdzcy = con.createStatement()) {

                // Pobieranie ID koni
                ResultSet rsKonie = stmtKonie.executeQuery(sqlKonie);
                while (rsKonie.next()) {
                    konieIds.add(rsKonie.getInt("id_konia"));
                }

                // Pobieranie ID jeźdźców
                ResultSet rsJezdzcy = stmtJezdzcy.executeQuery(sqlJezdzcy);
                while (rsJezdzcy.next()) {
                    jezdzcyIds.add(rsJezdzcy.getInt("id_jezdzcy"));
                }

                // Dla każdego konia przydzielamy jeźdźca i tworzymy nową gonitwę
                for (Integer konId : konieIds) {
                    int jezdziecId = jezdzcyIds.get(random.nextInt(jezdzcyIds.size()));

                    // Dodajemy nową gonitwę do tabeli gonitwy
                    try (PreparedStatement pstmtGonitwa = con.prepareStatement(sqlInsertGonitwa, Statement.RETURN_GENERATED_KEYS)) {
                        pstmtGonitwa.setString(1, "Nowa gonitwa");
                        pstmtGonitwa.setInt(2, 2024); // Przykładowa wartość dla sezonu
                        pstmtGonitwa.setDate(3, new Date(System.currentTimeMillis())); // aktualna data
                        pstmtGonitwa.setTime(4, Time.valueOf("12:00:00")); // przykładowy czas
                        pstmtGonitwa.executeUpdate();

                        // Pobieranie wygenerowanego ID gonitwy
                        int idGonitwy;
                        try (ResultSet generatedKeys = pstmtGonitwa.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                idGonitwy = generatedKeys.getInt(1);
                            } else {
                                throw new SQLException("Creating gonitwa failed, no ID obtained.");
                            }
                        }

                        // Dodawanie udziału w gonitwie
                        try (PreparedStatement pstmtUdzial = con.prepareStatement(sqlInsertUdzial)) {
                            pstmtUdzial.setInt(1, konId);
                            pstmtUdzial.setInt(2, idGonitwy);
                            pstmtUdzial.setInt(3, jezdziecId);
                            pstmtUdzial.setDouble(4, 1.5 + (4.5 - 1.5) * random.nextDouble()); // losowy kurs
                            pstmtUdzial.setInt(5, 0); // przykładowy wynik, wymaga dostosowania
                            pstmtUdzial.executeUpdate();
                        }
                    }
                }

                // Zatwierdzenie transakcji
                con.commit();
                 con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Błąd podczas układania harmonogramu: " + e.getMessage());
                try {
                    // Wycofanie transakcji jeśli wystąpił błąd
                    if (con != null && !con.getAutoCommit()) {
                        con.rollback();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                    System.out.println("Błąd podczas wycofywania transakcji: " + se.getMessage());
                }
            } finally {
                // Włączenie autoCommit
                if (con != null) {
                    try {
                        con.setAutoCommit(true);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



        @FXML
        private void handleHorseStats (ActionEvent event){
            statsVBox.setVisible(true);
            removeHorseVBox.setVisible(false);
            addvBox.setVisible(false);
            tableViewWinKupony.setVisible(false);
            winKuponyVBox.setVisible(false);

            ObservableList<StatystykiKonia> horseStats = FXCollections.observableArrayList();

            String sql = "SELECT u.id_udzialu, u.id_konia, u.id_jezdzcy, u.kurs, u.wynik_konia,u.id_gonitwy\n" +
                    "FROM udzial_w_gonitwach u JOIN konie k ON u.id_konia = k.id_konia;\n";

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
                 Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    StatystykiKonia statystyki = new StatystykiKonia();
                    // Ustawianie wartości dla obiektu statystyki
                    statystyki.setIdUdzialu(rs.getInt("id_udzialu"));
                    statystyki.setIdKonia(rs.getInt("id_konia"));
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

        private void filtrujStatystykiKoni (String filter, ObservableList < StatystykiKonia > horseStats){
            if (filter == null || filter.isEmpty()) {
                tableHorseStats.setItems(horseStats);
            } else {
                ObservableList<StatystykiKonia> filteredData = FXCollections.observableArrayList();
                for (StatystykiKonia stats : horseStats) {
                    // Zakładając, że filtrujemy po ID konia, które jest typu Integer
                    String idKoniaString = Integer.toString(stats.getIdKonia());
                    if (idKoniaString.contains(filter)) {
                        filteredData.add(stats);
                    }
                }
                tableHorseStats.setItems(filteredData);
            }
        }
        private void filtrujKonie (String filter, ObservableList < Horse > horsesList){
            if (filter == null || filter.isEmpty()) {
                tableHorses.setItems(horsesList);
            } else {
                ObservableList<Horse> filteredHorses = FXCollections.observableArrayList();
                for (Horse horse : horsesList) {
                    // Zakładając, że chcesz filtrować po imieniu konia
                    if (horse.getImieKonia().toLowerCase().contains(filter.toLowerCase())) {
                        filteredHorses.add(horse);
                    }
                }
                tableHorses.setItems(filteredHorses);
            }
        }


        @FXML
        private void handleConfirmWin (ActionEvent event){
            System.out.println("Potwierdzenie wygranej zostało kliknięte."); // Wypisanie na konsolę
            winKuponyVBox.setVisible(true);
            statsVBox.setVisible(false);
            addvBox.setVisible(false);
            tableViewWinKupony.setVisible(true);


            ObservableList<Kupon> kupony = FXCollections.observableArrayList();

            String sql = "SELECT * FROM kupony WHERE status_kuponu = true;";

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
                 Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Kupon kupon = new Kupon();
                    kupon.setIdKuponu(rs.getInt("id_kuponu"));
                    kupon.setIdUdzialu(rs.getInt("id_udzialu"));
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
        public void initialize () {

            //harmonogram
            idGonitwyColumnHarm.setCellValueFactory(new PropertyValueFactory<>("idGonitwy"));
            sezonColumnHarm.setCellValueFactory(new PropertyValueFactory<>("sezon"));
            opisGonitwyColumnHarm.setCellValueFactory(new PropertyValueFactory<>("opisGonitwy"));
            dataWysciguColumnHarm.setCellValueFactory(new PropertyValueFactory<>("dataWyscigu"));
            czasColumnHarm.setCellValueFactory(new PropertyValueFactory<>("czas"));


            idKoniaColumnn.setCellValueFactory(new PropertyValueFactory<>("idKonia"));
            imieKoniaColumn.setCellValueFactory(new PropertyValueFactory<>("imieKonia"));
            dataUrodzeniaColumn.setCellValueFactory(new PropertyValueFactory<>("dataUrodzenia"));
            rasaKoniaColumn.setCellValueFactory(new PropertyValueFactory<>("rasaKonia"));
            plecKoniaColumn.setCellValueFactory(new PropertyValueFactory<>("plecKonia"));


            idUdzialuColumn.setCellValueFactory(new PropertyValueFactory<>("idUdzialu"));
            idKoniaColumn.setCellValueFactory(new PropertyValueFactory<>("idKonia"));
            idGonitwyColumn.setCellValueFactory(new PropertyValueFactory<>("idGonitwy"));
            idJezdzcyColumn.setCellValueFactory(new PropertyValueFactory<>("idJezdzcy"));
            kursColumn.setCellValueFactory(new PropertyValueFactory<>("kurs"));
            wynikKoniaColumn.setCellValueFactory(new PropertyValueFactory<>("wynikKonia"));

            idKuponuColumn.setCellValueFactory(new PropertyValueFactory<>("idKuponu"));
            idUdzialuKuponColumn.setCellValueFactory(new PropertyValueFactory<>("idUdzialu"));
            kwotaColumn.setCellValueFactory(new PropertyValueFactory<>("kwota"));
            kursWinKuponyColumn.setCellValueFactory(new PropertyValueFactory<>("kurs"));
            statusKuponu.setCellValueFactory(new PropertyValueFactory<>("statusKuponu"));
            idUzytkownikaKupon.setCellValueFactory(new PropertyValueFactory<>("idUzytkownika"));


            TableColumn<Horse, Void> removeHorseColumn = new TableColumn<>("Usuń konia");
            removeHorseColumn.setCellFactory(param -> new TableCell<Horse, Void>() {
                private final Button btn = new Button("Usuń");

                {
                    Button removeHorseButton = new Button("Usuń");
                    removeHorseButton.setOnAction(e -> removeHorse());
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

            // Dodaj kolumnę do TableView koni
            tableHorses.getColumns().add(removeHorseColumn);

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
            //loadHorses();
            // loadKupony();
        }

        @FXML
        private void loadKupony () {
            winKuponyVBox.setVisible(true);
            tableViewWinKupony.setVisible(true);

            ObservableList<Kupon> kupony = FXCollections.observableArrayList();
            String sql = "SELECT id_kuponu, id_udzialu, kwota, kurs, status_kuponu, id_uzytkownika FROM kupony;";

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/HorseRace1", "administrator", "admin");
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Kupon kupon = new Kupon();
                    kupon.setIdKuponu(rs.getInt("id_kuponu"));
                    kupon.setIdUdzialu(rs.getInt("id_udzialu"));
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


        private void potwierdzKupon (Kupon kupon){
            winKuponyVBox.setVisible(true);
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




    }
