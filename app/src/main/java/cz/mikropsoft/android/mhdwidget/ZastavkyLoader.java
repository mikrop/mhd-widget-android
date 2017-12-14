package cz.mikropsoft.android.mhdwidget;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.databases.ZastavkaDao;
import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

/**
 * Loader pro načtení zastávek.
 */
public class ZastavkyLoader extends AsyncTaskLoader<List<Zastavka>> {

    private List<Zastavka> zastavky;
    private String query;
    private MhdRestClient restClient;

    /**
     * @param context aplikační kontext
     * @param bundle řetězec, proti kterému se budou zastávky hledat
     */
    public ZastavkyLoader(Context context, Bundle bundle, MhdRestClient restClient) {
        super(context);
        this.query = bundle.getString("queryFiler");
        this.restClient = restClient;
    }

    @Override
    public List<Zastavka> loadInBackground() {
        List<Zastavka> result = new ArrayList<>();

        ZastavkaDao zastavkaDao = MhdDatabase.getInstance(getContext()).zastavkaDao();
        List<Zastavka> zastavky = zastavkaDao.getAll();
        if (zastavky.isEmpty()) {
            zastavky = restClient.getZastavky().getBody(); // TODO[HAJEK] rest metodu volat pouze pokud neni uloženo v db
        }

        for (Zastavka zastavka : zastavky) {
            String normalize = Normalizer.normalize(zastavka.getJmeno(), Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
            if (!TextUtils.isEmpty(query) && normalize.toLowerCase().contains(query)) {
                result.add(zastavka);
            } else if (zastavka.isFavorite()) {
                result.add(zastavka);
            }
        }

        return result;
    }

    @Override
    public void deliverResult(List<Zastavka> zastavky) {
        if (isReset()) {
            onReleaseResources(zastavky);
        }
        List<Zastavka> oldData = this.zastavky;
        this.zastavky = zastavky;

        if (isStarted()) {
            super.deliverResult(zastavky);
        }

        if (oldData != null && oldData != zastavky) {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (zastavky != null) {
            deliverResult(zastavky);
        }

        if (takeContentChanged() || zastavky == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Zastavka> zastavky) {
        super.onCanceled(zastavky);
        onReleaseResources(zastavky);
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps' if needed.
        if (zastavky != null) {
            onReleaseResources(zastavky);
            zastavky = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<Zastavka> zastavky) {
        // For a simple List<> there is nothing to do. For something
        // like a Cursor, we would close it here.
    }

}
