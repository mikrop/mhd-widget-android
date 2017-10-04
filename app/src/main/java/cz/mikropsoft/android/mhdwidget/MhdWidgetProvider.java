package cz.mikropsoft.android.mhdwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.joda.time.LocalTime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;

@EReceiver
public class MhdWidgetProvider extends AppWidgetProvider {

    @RestService
    MhdWidgetClient restClient;
    @Pref
    MhdPreferences_ mhdPreferences;

    private static final String LOG_TAG = "MhdWidgetProvider";
    private static final Integer UPDATE_INTERVAL = 25; // Jak dlouho odpočet poběží
    public static final String ACTION_MHDWIDGET_UPDATE = "cz.mikropsoft.android.mhdwidget.action.MHDWIDGET_UPDATE"; // Musí se shodovat z action uvedenou v AndroidManifest.xml
    public static final String ACTION_MHDWIDGET_SEARCH = "cz.mikropsoft.android.mhdwidget.action.MHDWIDGET_SEARCH";

    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private AtomicInteger mIntervalCount = new AtomicInteger(1);

    public MhdWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_MHDWIDGET_UPDATE.equals(intent.getAction())) {
            mExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (mIntervalCount.getAndIncrement() == UPDATE_INTERVAL) {
                        mExecutor.shutdownNow();
                    }

                    updateAppWidget(context);
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
        if (ACTION_MHDWIDGET_SEARCH.equals(intent.getAction())) {
            Log.d(LOG_TAG, "ACTION_MHDWIDGET_SEARCH");
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
     */
    public void updateAppWidget(Context context) {
        int zastavkaId = mhdPreferences.zastavkaId().get();
        AktualniSpoj aktualniSpoj = restClient.getAktualniSpoj(zastavkaId, LocalTime.now()).getBody();

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
