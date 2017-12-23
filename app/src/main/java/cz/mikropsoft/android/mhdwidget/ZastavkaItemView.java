package cz.mikropsoft.android.mhdwidget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.databases.SpojDao;
import cz.mikropsoft.android.mhdwidget.databases.ZastavkaDao;
import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.Spoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@EViewGroup(R.layout.zastavka_list_item)
public class ZastavkaItemView extends LinearLayout {

    private static final String TAG = ZastavkaItemView.class.getName();

    @Pref
    MhdPreferences_ preferences;

    @ViewById
    TextView textViewLinka;
    @ViewById
    TextView textViewJmeno;
    @ViewById
    CheckBox checkBoxFavorite;

    @RestService
    MhdRestClient restClient;

    public ZastavkaItemView(Context context) {
        super(context);
    }

    public void bind(final Zastavka zastavka) {
        if (zastavka != null) {
            int zastavkaId = zastavka.getId();

            textViewLinka.setText(zastavka.getLinka());
            textViewLinka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MhdDatabase.isSpojEmpty(getContext(), zastavkaId)) {
                        Toast.makeText(getContext(), "Spoje zastávky nebyly dosud staženy", Toast.LENGTH_LONG).show();
                    } else {
                        JizdniRadActivity_.intent(getContext()).zastavkaId(zastavkaId).start();
                    }
                }
            });

            checkBoxFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v;
                    zastavka.setFavorite(cb.isChecked());
                    MhdDatabase database = MhdDatabase.getInstance(getContext());
                    ZastavkaDao zastavkaDao = database.zastavkaDao();
                    zastavkaDao.update(zastavka);

                    boolean favorite = cb.isChecked();
                    boolean spojEmpty = MhdDatabase.isSpojEmpty(getContext(), zastavkaId);
                    if (favorite) {
                        if (spojEmpty) {
                            loadAllSpoje(zastavkaId); // Stažení spojů k preferované zastávce
                        }
                    } else {
                        if (!spojEmpty) {
                            SpojDao spojDao = database.spojDao();
                            List<Spoj> spoje = spojDao.findByZastavkaId(zastavkaId);
                            spojDao.delete(spoje.toArray(new Spoj[spoje.size()]));
                            Log.d(TAG, "Spoje ze zastávky " + zastavka.getJmeno() + " byly smazány");
                        } else {
                            Log.d(TAG, "Spoje ze zastávky " + zastavka.getJmeno() + " již byly staženy");
                        }
                    }

                }
            });

            checkBoxFavorite.setChecked(zastavka.isFavorite());
            textViewJmeno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (MhdDatabase.isSpojEmpty(getContext(), zastavkaId)) {
                        Toast.makeText(getContext(), "Spoje zastávky nebyly dosud staženy", Toast.LENGTH_LONG).show();
                    } else {

                        preferences.edit().zastavkaId()
                                .put("" + zastavkaId)
                                .apply();

                        String text = getResources().getString(R.string.zobrazovana_label, zastavka.getJmeno());
                        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                    }

                }
            });
            textViewJmeno.setText(zastavka.getJmeno());
        }
    }

    @Background
    void loadAllSpoje(int zastavkaId) {
        Log.d(TAG, "Aktualozace spojů zastávky ID: " + zastavkaId);

        SpojDao spojDao = MhdDatabase.getInstance(getContext()).spojDao();
        if (MhdDatabase.isSpojEmpty(getContext(), zastavkaId)) {
            List<Spoj> spoje = restClient.getSpoje(zastavkaId).getBody();
            spojDao.insertAll(spoje);
            Log.d(TAG, "Uloženo " + spoje.size() + " spojů zastávky");
        } else {
            // TODO[HAJEK] Dořešit aktualizaci již uložených spojů
        }

        List<Spoj> result = spojDao.findByZastavkaId(zastavkaId);
        updateUI(result);
    }

    @UiThread
    void updateUI(List<Spoj> spoje) {
        Toast.makeText(getContext(), R.string.aktualozovan_seznam_spoju, Toast.LENGTH_LONG).show();
    }

}
