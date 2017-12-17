package cz.mikropsoft.android.mhdwidget;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.adapters.ZastavkaAdapter;
import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@EActivity
@OptionsMenu(R.menu.options_menu)
public class MainActivity extends ListActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = MainActivity.class.getName();

    @Pref
    MhdPreferences_ preferences;
    @Bean
    ZastavkaAdapter zastavkaAdapter;
    @RestService
    MhdRestClient restClient;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadFromRestClient();
                        Log.d(TAG, "Seznam zastávek byl aktualizován");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @OptionsItem(R.id.action_preferences)
    void preferencesStart() {
        MhdPreferenceActivity_.intent(this).start();
    }

    @Background
    public void loadFromRestClient() {
        List<Zastavka> nove = restClient.getZastavky().getBody();
        List<Zastavka> zastavky = MhdDatabase.zastavkyUpdate(this, nove);
        updateUI(zastavky);
    }

    @UiThread
    void updateUI(List<Zastavka> zastavky) {
        zastavkaAdapter.setData(zastavky);
    }

    @AfterViews
    void afterViews() {
        zastavkaAdapter.setData(MhdDatabase.getInstance(this).zastavkaDao().getAll());
        getListView().setAdapter(zastavkaAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    //--- OnQueryTextListener methods --------------------------------------------------------------

    @Override
    public boolean onQueryTextChange(String newText) {
        String constraint = !TextUtils.isEmpty(newText) ? newText : null;
        zastavkaAdapter.getFilter().filter(constraint);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //--- Item -------------------------------------------------------------------------------------

    @ItemClick
    void listItemClicked(Zastavka zastavka) {

        int zastavkaId = zastavka.getId();
        if (MhdDatabase.isSpojEmpty(getApplicationContext(), zastavkaId)) {
            Toast.makeText(this, "Spoje zastávky nebyly dosud staženy", Toast.LENGTH_LONG).show();
        } else {

            preferences.edit().zastavkaId()
                    .put("" + zastavkaId)
                    .apply();

            String text = getResources().getString(R.string.zobrazovana_label, zastavka.getJmeno());
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

}

