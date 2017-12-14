package cz.mikropsoft.android.mhdwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;

@EReceiver
public class MhdWidgetProvider extends AppWidgetProvider {

    @RestService
    MhdRestClient restClient;
    @Pref
    MhdPreferences_ preferences;

    private static final String TAG = MhdWidgetProvider.class.getName();
    private static final Integer UPDATE_INTERVAL = 25; // Jak dlouho odpočet poběží
    public static final String ACTION_MHDWIDGET_UPDATE = "cz.mikropsoft.android.mhdwidget.action.MHDWIDGET_UPDATE"; // Musí se shodovat z action uvedenou v AndroidManifest.xml
    public static final String ACTION_MHDWIDGET_SEARCH = "cz.mikropsoft.android.mhdwidget.action.MHDWIDGET_SEARCH";

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private AtomicInteger updateCounter = new AtomicInteger();

    public MhdWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_MHDWIDGET_UPDATE.equals(intent.getAction())) {

            int zastavkaId = Integer.parseInt(preferences.zastavkaId().get());
            final AktualniSpoj aktualniSpoj = MhdDatabase.getAktualniSpoj(context, zastavkaId);

            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (updateCounter.getAndIncrement() == UPDATE_INTERVAL) {
                        executor.shutdownNow();
                        updateAppWidget(context, null); // Aby se widget nastavil do výchozích hodnot
                    } else {
                        updateAppWidget(context, aktualniSpoj);
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
        if (ACTION_MHDWIDGET_SEARCH.equals(intent.getAction())) {
            Log.d(TAG, "ACTION_MHDWIDGET_SEARCH");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Intent intent = new Intent(context, MhdWidgetProvider_.class);
        intent.setAction(ACTION_MHDWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Intent searchIntent = new Intent(context, MainActivity_.class);
        intent.setAction(ACTION_MHDWIDGET_SEARCH);
        PendingIntent searchPendingIntent = PendingIntent.getActivity(context, 0, searchIntent, 0);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mhd_widget);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.search_button, searchPendingIntent);

        ComponentName provider = new ComponentName(context, MhdWidgetProvider_.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(provider, remoteViews);
    }

    /**
     * Aktualizace widgetu na uživatelem vyvolanou akci.
     *
     * @param context aplikační kontext
     * @param aktualniSpoj aktuální spoj
     */
    public void updateAppWidget(Context context, @Nullable AktualniSpoj aktualniSpoj) {

        Resources resources = context.getResources();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mhd_widget);
        remoteViews.setTextViewText(R.id.widget_zastavka_jmeno, (aktualniSpoj != null) ? aktualniSpoj.getZastavka() : resources.getText(R.string.zastavka_label));
        remoteViews.setTextViewText(R.id.widget_predchozi, (aktualniSpoj != null) ? SpojFormatter.printOdjezd(aktualniSpoj.getPredchozi()) : resources.getText(R.string.widget_predchozi));
        remoteViews.setTextViewText(R.id.widget_zbyva_casu, (aktualniSpoj != null) ? SpojFormatter.printZbyvaCasu(aktualniSpoj) : resources.getText(R.string.widget_zbyva_casu));
        remoteViews.setTextViewText(R.id.widget_nasledujici, (aktualniSpoj != null) ? SpojFormatter.printOdjezd(aktualniSpoj.getNasledujici()) : resources.getText(R.string.widget_nasledujici));

        ComponentName provider = new ComponentName(context, MhdWidgetProvider_.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(provider, remoteViews);
    }

}
