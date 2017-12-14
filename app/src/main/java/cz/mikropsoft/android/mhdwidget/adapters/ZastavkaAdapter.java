package cz.mikropsoft.android.mhdwidget.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.ZastavkaItemView;
import cz.mikropsoft.android.mhdwidget.ZastavkaItemView_;
import cz.mikropsoft.android.mhdwidget.databases.ZastavkaDao;
import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@EBean
public class ZastavkaAdapter extends BaseAdapter {

    private static final String TAG = ZastavkaAdapter.class.getName();

    List<Zastavka> zastavky = new ArrayList<>();
    String query;

    @RootContext
    Context context;

    @RestService
    MhdRestClient restClient;

    @AfterInject
    void afterInject() {
        loadAllZastavky();
    }

    @Override
    public int getCount() {
        return zastavky.size();
    }

    @Override
    public Zastavka getItem(int position) {
        return zastavky.get(position);
    }

    @Override
    public long getItemId(int position) {
        return zastavky.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ZastavkaItemView zastavkaItemView;
        if (convertView == null) {
            zastavkaItemView = ZastavkaItemView_.build(context);
        } else {
            zastavkaItemView = (ZastavkaItemView) convertView;
        }
        zastavkaItemView.bind(getItem(position));
        return zastavkaItemView;

    }

    @Background
    void loadAllZastavky() {

        ZastavkaDao zastavkaDao = MhdDatabase.getInstance(context).zastavkaDao();
        if (zastavkaDao.getAll().isEmpty()) {
            List<Zastavka> zastavky = restClient.getZastavky().getBody();
            Log.d(TAG, "Načteno " + zastavky.size() + " zastávek z aplikačního serveru");
            zastavkaDao.insertAll(zastavky);
        }

        List<Zastavka> result = zastavkaDao.getAll();
        updateUI(result);
    }

    @UiThread
    void updateUI(List<Zastavka> result) {
        ZastavkaAdapter.this.zastavky = result;
        notifyDataSetChanged();
    }

    public void setData(List<Zastavka> zastavky) {
        this.zastavky = zastavky;
        notifyDataSetChanged();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        query = query;
    }

}
