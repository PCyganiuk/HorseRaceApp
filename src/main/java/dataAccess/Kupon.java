package dataAccess;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Kupon {
    private SimpleIntegerProperty idKuponu;
    private SimpleIntegerProperty idUdzialu;
    private SimpleDoubleProperty kwota;
    private SimpleDoubleProperty kurs;
    private SimpleBooleanProperty statusKuponu;
    private SimpleIntegerProperty idUzytkownika;

    public int getIdKuponu() {
        return idKuponu.get();
    }

    public SimpleIntegerProperty idKuponuProperty() {
        return idKuponu;
    }

    public void setIdKuponu(int idKuponu) {
        this.idKuponu.set(idKuponu);
    }

    public int getIdUdzialu() {
        return idUdzialu.get();
    }

    public SimpleIntegerProperty idUdzialuProperty() {
        return idUdzialu;
    }

    public void setIdUdzialu(int idUdzialu) {
        this.idUdzialu.set(idUdzialu);
    }

    public double getKwota() {
        return kwota.get();
    }

    public SimpleDoubleProperty kwotaProperty() {
        return kwota;
    }

    public void setKwota(double kwota) {
        this.kwota.set(kwota);
    }

    public double getKurs() {
        return kurs.get();
    }

    public SimpleDoubleProperty kursProperty() {
        return kurs;
    }

    public void setKurs(double kurs) {
        this.kurs.set(kurs);
    }

    public boolean isStatusKuponu() {
        return statusKuponu.get();
    }

    public SimpleBooleanProperty statusKuponuProperty() {
        return statusKuponu;
    }

    public void setStatusKuponu(boolean statusKuponu) {
        this.statusKuponu.set(statusKuponu);
    }

    public int getIdUzytkownika() {
        return idUzytkownika.get();
    }

    public SimpleIntegerProperty idUzytkownikaProperty() {
        return idUzytkownika;
    }

    public void setIdUzytkownika(int idUzytkownika) {
        this.idUzytkownika.set(idUzytkownika);
    }
}

