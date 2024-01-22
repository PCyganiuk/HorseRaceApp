package dataAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorsesRepository extends Repository<Horse> {

    public HorsesRepository(Connection connection) {
        super(connection);
    }

    @Override
    public List<Horse> getAll() throws SQLException {
        List<Horse> horses = new ArrayList<>();
        String query = "SELECT id_konia, imie_konia, data_urodzenia, rasa_konia, plec_konia FROM konie";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Horse horse = new Horse();
                horse.setIdKonia(rs.getInt("id_konia"));
                horse.setImieKonia(rs.getString("imie_konia"));
                horse.setDataUrodzenia(rs.getDate("data_urodzenia").toLocalDate());
                horse.setRasaKonia(rs.getString("rasa_konia"));
                horse.setPlecKonia(rs.getString("plec_konia"));

                horses.add(horse);
            }
        }
        return horses;
    }


    @Override

    Horse getById(int id) throws SQLException {
        Horse horse = null;
        String query = "SELECT id_konia, imie_konia, data_urodzenia, rasa_konia, plec_konia FROM konie WHERE id_konia = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                horse = new Horse();
                horse.setIdKonia(rs.getInt("id_konia"));
                horse.setImieKonia(rs.getString("imie_konia"));
                horse.setDataUrodzenia(rs.getDate("data_urodzenia").toLocalDate());
                horse.setRasaKonia(rs.getString("rasa_konia"));
                horse.setPlecKonia(rs.getString("plec_konia"));

            }
        } catch (SQLException e) {

            throw e;
        }

        return horse;
    }


    @Override
    void insert(Horse entity) throws SQLException {
        String query = "INSERT INTO konie (imie_konia, data_urodzenia, rasa_konia, plec_konia) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getImieKonia());
            pstmt.setDate(2, Date.valueOf(entity.getDataUrodzenia())); // zakładając, że getDataUrodzenia zwraca java.time.LocalDate
            pstmt.setString(3, entity.getRasaKonia());
            pstmt.setString(4, entity.getPlecKonia());

            pstmt.executeUpdate();
        }
    }


    @Override
    void update(Horse entity) throws SQLException {
        String query = "UPDATE konie SET imie_konia = ?, data_urodzenia = ?, rasa_konia = ?, plec_konia = ? WHERE id_konia = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getImieKonia());
            pstmt.setDate(2, Date.valueOf(entity.getDataUrodzenia()));
            pstmt.setString(3, entity.getRasaKonia());
            pstmt.setString(4, entity.getPlecKonia());
            pstmt.setInt(5, entity.getIdKonia());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aktualizacja konia nie powiodła się, żaden wiersz nie został zmieniony.");
            }
        }
    }


    @Override
    void removeById(int id) throws SQLException {
        String query = "DELETE FROM konie WHERE id_konia = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Usuwanie konia nie powiodło się, żaden wiersz nie został zmieniony.");
            }
        }
    }

}
