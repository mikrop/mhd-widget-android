package cz.mikropsoft.android.mhdwidget;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.sharedpreferences.IntPrefField;
import org.androidannotations.api.sharedpreferences.StringPrefField;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@EActivity
public class MainActivity extends ListActivity
        implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<List<Zastavka>> {

    @RestService
    MhdWidgetClient restClient;
    @Pref
    MhdPreferences_ mhdPreferences;

    private ZastavkaSearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ZastavkaSearchAdapter(this, new ArrayList<Zastavka>());
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_preferences:
                Intent intent = new Intent(this, MainPreferenceActivity_.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @ItemClick
    void listItemClicked(Zastavka zastavka) {
        mhdPreferences.edit().zastavkaId()
                .put(zastavka.getId())
                .apply();

        String text = this.getResources().getText(R.string.dialog_title) + ": " + zastavka.getJmeno();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

//    @ItemLongClick
//    void listItemLongClicked(Zastavka zastavka) {
//        Toast.makeText(this, "long click: " + zastavka.getJmeno(), Toast.LENGTH_SHORT).show();
//    }

    @ItemSelect
    void listItemSelected(boolean somethingSelected, Zastavka zastavka) {
        if (somethingSelected) {
            Toast.makeText(this, "selected: " + zastavka.getJmeno(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "nothing selected", Toast.LENGTH_SHORT).show();
        }
    }

    // OnQueryTextListener methods -----------------------------------------------------------------

    @Override
    public boolean onQueryTextChange(String newText) {
        String queryFiler = !TextUtils.isEmpty(newText) ? newText : null;
        mAdapter.setQuery(queryFiler);

        Bundle args = new Bundle();
        args.putString("queryFiler", queryFiler);
//        args.putInt("zastavkaId", mainPreference.zastavkaId().get());
        getLoaderManager().restartLoader(0, args, this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // LoaderManager methods -----------------------------------------------------------------------

    @Override
    public Loader<List<Zastavka>> onCreateLoader(int i, Bundle bundle) {
        return new ZastavkyLoader(this, bundle, restClient);
    }

    @Override
    public void onLoadFinished(Loader<List<Zastavka>> loader, List<Zastavka> zastavky) {
        mAdapter.setData(zastavky);
    }

    @Override
    public void onLoaderReset(Loader<List<Zastavka>> loader) {
        mAdapter.setData(new ArrayList<Zastavka>());
    }

//    @Click({R.layout.mhd_widget})
//    void updateWidget() {
//
//        int zastavkaId = mainPreference.zastavkaId().get();
//        ResponseEntity<AktualniSpoj> aktualniSpoj = restClient.getAktualniSpoj(zastavkaId);
//        System.out.println();

//        int zastavkaId = mainPreference.zastavkaId().get();
//        searchAsync(zastavkaId);
//    }
//
//    @Background
//    void searchAsync(int zastavkaId) {
//        int zastavkaId = mainPreference.zastavkaId().get();
//        Iterator<Zastavka> iterator = restClient.getZastavky().iterator();
//        List<Zastavka> zastavky = new ArrayList<>();
//        while (iterator.hasNext()) {
//            zastavky.add(iterator.next());
//        }
//        mAdapter.setData(zastavky);
//    }
//
//    @UiThread
//    void afterSearch(ResponseEntity<AktualniSpoj> entity) {
//        if (entity != null && entity.getBody() != null) {
//            textViewZastavka.setText(entity.getBody().getZastavka());
//        }
//    }

}
