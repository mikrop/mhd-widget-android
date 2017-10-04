package cz.mikropsoft.android.mhdwidget.model;

/**
 * Projekce zastávky.
 */
public class Zastavka {

    private int id;
    private String jmeno;
    private String smer;
    private boolean selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getSmer() {
        return smer;
    }

    public void setSmer(String smer) {
        this.smer = smer;
    }

    /**
     * Příznak preferované zastávky. Zastávky u které bylo kliknuto na "hvězdičku".
     *
     * @return {@code true} preferovan zastávka, jinak {@code false}
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Nastaví tuto zastávku jako preferovanou.
     *
     * @param selected {@code true} pokud jde o preferovanou zastávka, jinak {@code false}
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
