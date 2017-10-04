package cz.mikropsoft.android.mhdwidget;

import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Assert;

import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;

/**
 * Pomocné metody formátující {@link AktualniSpoj} pro tiskový výstup.
 */
public class SpojFormatter {

    private static final PeriodFormatter pf = new PeriodFormatterBuilder()
            .appendLiteral("Odjezd ")
            .appendDays().appendSuffix("d")
            .appendHours().appendSuffix("h")
            .printZeroAlways()
            .appendMinutes().appendSuffix("m")
            .minimumPrintedDigits(2).appendSeconds().appendSuffix("s")
            .toFormatter();

    private static final DateTimeFormatter of = new DateTimeFormatterBuilder()
            .appendPattern("HH:mm:ss").toFormatter();

    /**
     * Vytiskne formátovanou informace o zbývajícím čase do následujícího odjezdu.
     *
     * @return formátovaná informace.
     */
    public static String printZbyvaCasu(AktualniSpoj aktualniSpoj) {
        Assert.assertNotNull(aktualniSpoj);

        LocalTime odjezd = aktualniSpoj.getOdjezd();
        Period period = new Period(LocalTime.now(), odjezd);
        return pf.print(period);
    }

    /**
     * Vytiskne formátovanou informace o aktuálním čase odjezdu.
     *
     * @return formátovaná informace.
     */
    public static String printOdjezd(LocalTime odjezd) {
        Assert.assertNotNull(odjezd);

        return of.print(odjezd);
    }

}
