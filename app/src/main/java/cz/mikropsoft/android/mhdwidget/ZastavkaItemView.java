package cz.mikropsoft.android.mhdwidget;

import android.app.ProgressDialog;
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
import java.util.Objects;

import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.databases.SpojDao;
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

    private ProgressDialog progressDialog;

    public ZastavkaItemView(Context context) {
        super(context);
    }

    public void bind(final Zastavka zastavka) {
        if (zastavka != null) {
            int zastavkaId = zastavka.getId();

//            textViewLinka.setText(zastavka.getLinka());
            textViewLinka.setBackgroundResource(zastavka.getProstredek().getResid());
            textViewLinka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MhdDatabase.isSpojEmpty(getContext(), zastavkaId)) {
                        Toast.makeText(getContext(), "Jízdní řád nebyl ještě stažen", Toast.LENGTH_LONG).show();
                    } else {
                        JizdniRadActivity_.intent(getContext()).zastavkaId(zastavkaId).start();
                    }
                }
            });

//            if (zastavkaId == Integer.parseInt(preferences.zastavkaId().get())) {
//                textViewJmeno.setTypeface(textViewJmeno.getTypeface(), Typeface.BOLD);
//            }
            textViewJmeno.setText(zastavka.getJmeno());
            textViewJmeno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (MhdDatabase.isSpojEmpty(getContext(), zastavkaId)) {
                        Toast.makeText(getContext(), "Jízdní řád nebyl ještě stažen", Toast.LENGTH_LONG).show();
                    } else {

                        preferences.edit().zastavkaId()
                                .put(Objects.toString(zastavkaId))
                                .apply();

                        String text = getResources().getString(R.string.zobrazovana_label, zastavka.getJmeno());
                        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                    }
                }
            });

            checkBoxFavorite.setChecked(zastavka.isFavorite());
            checkBoxFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v;
                    boolean spojEmpty = MhdDatabase.isSpojEmpty(getContext(), zastavkaId);
                    if (cb.isChecked()) {
                        if (spojEmpty) {
                            progressDialog = ProgressDialog.show(getContext(), "Stahování",
                                    "Jízdního řádu...", true);

                            loadAllSpoje(zastavkaId); // Stažení spojů k preferované zastávce
                        }
                    } else {
                        if (!spojEmpty) {

                            SpojDao spojDao = MhdDatabase.getInstance(getContext()).spojDao();
                            spojDao.delete(spojDao.findByZastavkaId(zastavkaId));
                            MhdDatabase.setFavorite(getContext(), zastavkaId, false);

                            Log.d(TAG, "Spoje ze zastávky " + zastavka.getJmeno() + " byly smazány");

                        } else {
                            Log.d(TAG, "Spoje ze zastávky " + zastavka.getJmeno() + " již byly staženy");
                        }
                    }
                }
            });
        }
    }

    @Background
    void loadAllSpoje(int zastavkaId) {
        Log.d(TAG, "Aktualizace spojů zastávky ID: " + zastavkaId);

        if (MhdDatabase.isSpojEmpty(getContext(), zastavkaId)) {

            List<Spoj> spoje = restClient.getSpoje(zastavkaId).getBody();
            SpojDao spojDao = MhdDatabase.getInstance(getContext()).spojDao();
            spojDao.insertAll(spoje);
            Log.d(TAG, "Uloženo " + spoje.size() + " spojů zastávky");

        } else {
            // TODO[HAJEK] Dořešit aktualizaci již uložených spojů
        }

        updateUI(zastavkaId);
    }

    @UiThread
    void updateUI(int zastavkaId) {
        progressDialog.dismiss();

        MhdDatabase.setFavorite(getContext(), zastavkaId, true);
        Toast.makeText(getContext(), R.string.jizdni_rad_stazen, Toast.LENGTH_LONG).show();
    }

}
