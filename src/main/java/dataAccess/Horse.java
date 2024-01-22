package dataAccess;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Horse extends Entity {

    private SimpleIntegerProperty idKonia =new SimpleIntegerProperty(this, "id");
    private SimpleStringProperty imieKonia= new SimpleStringProperty(this, "imieKonia");
    private ObjectProperty<LocalDate> dataUrodzenia = new SimpleObjectProperty<>(this, "data_urodzenia");

    private SimpleStringProperty plecKonia = new SimpleStringProperty(this, "plec_konia");
    private SimpleStringProperty rasaKonia = new SimpleStringProperty(this, "rasa_konia");

    @Override
    public int getIdKonia() {
        return idKonia.get();
    }

    public SimpleIntegerProperty idKoniaProperty() {
        return idKonia;
    }

    public void setIdKonia(int idKonia) {
        this.idKonia.set(idKonia);
    }

    public String getImieKonia() {
        return imieKonia.get();
    }

    public SimpleStringProperty imieKoniaProperty() {
        return imieKonia;
    }

    public void setImieKonia(String imieKonia) {
        this.imieKonia.set(imieKonia);
    }

    public LocalDate getDataUrodzenia() {
        return dataUrodzenia.get();
    }

    public ObjectProperty<LocalDate> dataUrodzeniaProperty() {
        return dataUrodzenia;
    }

    public void setDataUrodzenia(LocalDate dataUrodzenia) {
        this.dataUrodzenia.set(dataUrodzenia);
    }

    public String getPlecKonia() {
        return plecKonia.get();
    }

    public SimpleStringProperty plecKoniaProperty() {
        return plecKonia;
    }

    public void setPlecKonia(String plecKonia) {
        this.plecKonia.set(plecKonia);
    }

    public String getRasaKonia() {
        return rasaKonia.get();
    }

    public SimpleStringProperty rasaKoniaProperty() {
        return rasaKonia;
    }

    public void setRasaKonia(String rasaKonia) {
        this.rasaKonia.set(rasaKonia);
    }
}
