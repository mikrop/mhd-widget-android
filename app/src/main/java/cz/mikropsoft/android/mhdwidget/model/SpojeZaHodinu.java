package cz.mikropsoft.android.mhdwidget.model;

import android.support.annotation.NonNull;

import org.junit.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Řádek hodinového jízdního řádu.
 */
public class SpojeZaHodinu implements Comparable<SpojeZaHodinu> {

    Integer hodinaDne;
    List<Spoj> spoje;

    public SpojeZaHodinu(List<Spoj> spoje) {
        Assert.assertNotNull(spoje);
        Assert.assertFalse(spoje.isEmpty());

        this.hodinaDne = spoje.stream()
                .sorted(Collections.reverseOrder())
                .map(spoj -> spoj.getOdjezd().getHourOfDay())
                .findFirst()
                .orElse(null);
        this.spoje = spoje;
    }

    /**
     * Hodina dne.
     *
     * @return hodina dne
     */
    public Integer getHodinaDne() {
        return hodinaDne;
    }

    /**
     * {@link Spoj}e, které v {@link #hodinaDne} jedou.
     *
     * @return {@link Spoj}e v hodině
     */
    public List<Spoj> getSpoje() {
        return spoje;
    }

    @Override
    public int compareTo(@NonNull SpojeZaHodinu o) {
        return o.getHodinaDne().compareTo(getHodinaDne());
    }

}
