package dataAccess;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class StatystykiKonia {
    private IntegerProperty idUdzialu = new SimpleIntegerProperty();
    private IntegerProperty idKonia = new SimpleIntegerProperty();
    private IntegerProperty idGonitwy = new SimpleIntegerProperty();
    private IntegerProperty idJezdzcy = new SimpleIntegerProperty();
    private DoubleProperty kurs = new SimpleDoubleProperty();
    private IntegerProperty wynikKonia = new SimpleIntegerProperty();

    public int getIdUdzialu() {
        return idUdzialu.get();
    }

    public IntegerProperty idUdzialuProperty() {
        return idUdzialu;
    }

    public void setIdUdzialu(int idUdzialu) {
        this.idUdzialu.set(idUdzialu);
    }

    public int getIdKonia() {
        return idKonia.get();
    }

    public IntegerProperty idKoniaProperty() {
        return idKonia;
    }

    public void setIdKonia(int idKonia) {
        this.idKonia.set(idKonia);
    }

    public int getIdGonitwy() {
        return idGonitwy.get();
    }

    public IntegerProperty idGonitwyProperty() {
        return idGonitwy;
    }

    public void setIdGonitwy(int idGonitwy) {
        this.idGonitwy.set(idGonitwy);
    }

    public int getIdJezdzcy() {
        return idJezdzcy.get();
    }

    public IntegerProperty idJezdzcyProperty() {
        return idJezdzcy;
    }

    public void setIdJezdzcy(int idJezdzcy) {
        this.idJezdzcy.set(idJezdzcy);
    }

    public double getKurs() {
        return kurs.get();
    }

    public DoubleProperty kursProperty() {
        return kurs;
    }

    public void setKurs(double kurs) {
        this.kurs.set(kurs);
    }

    public int getWynikKonia() {
        return wynikKonia.get();
    }

    public IntegerProperty wynikKoniaProperty() {
        return wynikKonia;
    }

    public void setWynikKonia(int wynikKonia) {
        this.wynikKonia.set(wynikKonia);
    }
}
