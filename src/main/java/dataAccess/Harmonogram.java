package dataAccess;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Harmonogram {
    private final IntegerProperty idGonitwy = new SimpleIntegerProperty(this, "idGonitwy");
    private final IntegerProperty sezon = new SimpleIntegerProperty(this, "idKonia");
    private final StringProperty opisGonitwy = new SimpleStringProperty(this, "idJezdzcy");
    private final ObjectProperty<LocalDate> dataWyscigu = new SimpleObjectProperty<>(this, "dataGonitwy");
    private final ObjectProperty<Double> czas = new SimpleObjectProperty<>(this, "kurs");

    public int getIdGonitwy() {
        return idGonitwy.get();
    }

    public IntegerProperty idGonitwyProperty() {
        return idGonitwy;
    }

    public void setIdGonitwy(int idGonitwy) {
        this.idGonitwy.set(idGonitwy);
    }

    public int getSezon() {
        return sezon.get();
    }

    public IntegerProperty sezonProperty() {
        return sezon;
    }

    public void setSezon(int sezon) {
        this.sezon.set(sezon);
    }

    public String getOpisGonitwy() {
        return opisGonitwy.get();
    }

    public StringProperty opisGonitwyProperty() {
        return opisGonitwy;
    }

    public void setOpisGonitwy(String opisGonitwy) {
        this.opisGonitwy.set(opisGonitwy);
    }

    public LocalDate getDataWyscigu() {
        return dataWyscigu.get();
    }

    public ObjectProperty<LocalDate> dataWysciguProperty() {
        return dataWyscigu;
    }

    public void setDataWyscigu(LocalDate dataWyscigu) {
        this.dataWyscigu.set(dataWyscigu);
    }

    public Double getCzas() {
        return czas.get();
    }

    public ObjectProperty<Double> czasProperty() {
        return czas;
    }

    public void setCzas(Double czas) {
        this.czas.set(czas);
    }
}
