package dataAccess;

import java.time.LocalDate;

public class Horse extends Entity {
    private String name;
    private int id;
    private String imieKonia;
    private LocalDate data_urodzenia;
    private String plec_konia;
    private String rasa_konia;

    public String getImieKonia() {
        return imieKonia;
    }

    public void setImieKonia(String imieKonia) {
        this.imieKonia = imieKonia;
    }

    public LocalDate getData_urodzenia() {
        return data_urodzenia;
    }

    public void setData_urodzenia(LocalDate data_urodzenia) {
        this.data_urodzenia = data_urodzenia;
    }

    public String getPlec_konia() {
        return plec_konia;
    }

    public void setPlec_konia(String plec_konia) {
        this.plec_konia = plec_konia;
    }

    public String getName() {
        return name;
    }

    public String getRasa_konia() {
        return rasa_konia;
    }

    public void setRasa_konia(String rasa_konia) {
        this.rasa_konia = rasa_konia;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
