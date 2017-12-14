package cz.mikropsoft.android.mhdwidget.model;

import org.joda.time.LocalTime;

/**
 * Wrapper slučující informace ze zastávky a předaného spoje.
 */
public class AktualniSpoj {

    private Zastavka zastavka;
    private Spoj spoj;

    public AktualniSpoj(Zastavka zastavka, Spoj spoj) {
        this.zastavka = zastavka;
        this.spoj = spoj;
    }

    public String getSmer() {
        return zastavka.getSmer();
    }

    public void getSmer(String smer) {
        zastavka.setSmer(smer);
    }

    public String getZastavka() {
        return zastavka.getJmeno();
    }

    public void setZastavka(String jmeno) {
        zastavka.setJmeno(jmeno);
    }

    public LocalTime getPredchozi() {
        return spoj.getPredchozi();
    }

    public void setPredchozi(LocalTime predchozi) {
        spoj.setPredchozi(predchozi);
    }

    public LocalTime getOdjezd() {
        return spoj.getOdjezd();
    }

    public void setOdjezd(LocalTime odjezd) {
        spoj.setOdjezd(odjezd);
    }

    public LocalTime getNasledujici() {
        return spoj.getNasledujici();
    }

    public void setNasledujici(LocalTime nasledujici) {
        spoj.setNasledujici(nasledujici);
    }

}
