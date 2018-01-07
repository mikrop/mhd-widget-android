package cz.mikropsoft.android.mhdwidget;

import android.app.ListActivity;
import android.os.Bundle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.rest.spring.annotations.RestService;
import org.joda.time.LocalTime;

import cz.mikropsoft.android.mhdwidget.adapters.JizdniRadAdapter;
import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@EActivity(R.layout.activity_jizdni_rad)
public class JizdniRadActivity extends ListActivity {

    private static final String TAG = JizdniRadActivity.class.getName();

    @Bean
    JizdniRadAdapter jizdniRadAdapter;
    @RestService
    MhdRestClient restClient;
    @Extra
    int zastavkaId;

//    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jizdni_rad);

        Zastavka zastavka = MhdDatabase.getInstance(this).zastavkaDao().finOne(zastavkaId);
        this.setTitle("(" + zastavka.getLinka() +") " + zastavka.getJmeno() + ", " + zastavka.getSmer());

//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        swipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        Log.d(TAG, "Jízdní řád byl aktualizován");
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//        );
    }

    @AfterViews
    void afterViews() {
        jizdniRadAdapter.setData(zastavkaId);
        getListView().setAdapter(jizdniRadAdapter);
        getListView().setSelection(LocalTime.now().getHourOfDay()); // Posun na aktuální hodinu
    }

}
