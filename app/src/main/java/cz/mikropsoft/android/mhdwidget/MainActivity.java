package cz.mikropsoft.android.mhdwidget;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.adapters.ZastavkaAdapter;
import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@EActivity(R.layout.main)
@OptionsMenu(R.menu.options_menu)
public class MainActivity extends ListActivity implements SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<List<Zastavka>> {

    @Pref
    MhdPreferences_ preferences;
    @Bean
    ZastavkaAdapter zastavkaAdapter;
    @RestService
    MhdRestClient restClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OptionsItem(R.id.action_preferences)
    void preferencesStart() {
        MhdPreferenceActivity_.intent(this).start();
    }

    @AfterViews
    void afterViews() {
//        getListView().setEmptyView(findViewById(android.R.id.empty));
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
        String queryFiler = !TextUtils.isEmpty(newText) ? newText : null;
        zastavkaAdapter.setQuery(queryFiler);

        Bundle args = new Bundle();
        args.putString("queryFiler", queryFiler);
        getLoaderManager().restartLoader(0, args, this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //--- LoaderManager methods --------------------------------------------------------------------

    @Override
    public Loader<List<Zastavka>> onCreateLoader(int i, Bundle bundle) {
        return new ZastavkyLoader(this, bundle, restClient);
    }

    @Override
    public void onLoadFinished(Loader<List<Zastavka>> loader, List<Zastavka> zastavky) {
        zastavkaAdapter.setData(zastavky);
    }

    @Override
    public void onLoaderReset(Loader<List<Zastavka>> loader) {
        zastavkaAdapter.setData(new ArrayList<Zastavka>());
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

