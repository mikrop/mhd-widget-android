package cz.mikropsoft.android.mhdwidget.model;

import android.support.annotation.NonNull;

import org.joda.time.LocalTime;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Jeden řádek pohledu na zastávkový jízdní řád.
 */
public class JizdniRad implements Comparable<JizdniRad> {

    private LocalTime hodina;
    private List<LocalTime> minuty;

    public JizdniRad(List<Spoj> spoje) {
        Assert.assertNotNull(spoje);
        Assert.assertFalse(spoje.isEmpty());

        this.hodina = spoje.iterator().next().getOdjezd();
        this.minuty = spoje.stream()
                .map(Spoj::getOdjezd)
                .collect(Collectors.toList());
    }

    public LocalTime getHodina() {
        return hodina;
    }

    public List<LocalTime> getMinuty() {
        return minuty;
    }

    @Override
    public int compareTo(@NonNull JizdniRad o) {
        return o.getHodina().compareTo(getHodina());
    }

}
