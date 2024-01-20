package dataAccess;

public class StatystykiKonia {
    private Horse kon;
    private int idUdzialu;
    private int idGonitwy;
    private int idJezdzcy;
    private double kurs;
    private int wynikKonia;

    public Horse getKon() {
        return kon;
    }

    public void setKon(Horse kon) {
        this.kon = kon;
    }

    public int getIdUdzialu() {
        return idUdzialu;
    }

    public void setIdUdzialu(int idUdzialu) {
        this.idUdzialu = idUdzialu;
    }

    public int getIdGonitwy() {
        return idGonitwy;
    }

    public void setIdGonitwy(int idGonitwy) {
        this.idGonitwy = idGonitwy;
    }

    public int getIdJezdzcy() {
        return idJezdzcy;
    }

    public void setIdJezdzcy(int idJezdzcy) {
        this.idJezdzcy = idJezdzcy;
    }

    public double getKurs() {
        return kurs;
    }

    public void setKurs(double kurs) {
        this.kurs = kurs;
    }

    public int getWynikKonia() {
        return wynikKonia;
    }

    public void setWynikKonia(int wynikKonia) {
        this.wynikKonia = wynikKonia;
    }
}
