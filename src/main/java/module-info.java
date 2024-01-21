module com.example.horseraceapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.horseraceapp to javafx.fxml;
    opens dataAccess to javafx.fxml, javafx.base;
    exports com.example.horseraceapp;
}