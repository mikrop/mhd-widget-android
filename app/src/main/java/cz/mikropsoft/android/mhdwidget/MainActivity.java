package cz.mikropsoft.android.mhdwidget;

import android.app.Activity;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;

import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    // Testovací ID zastávky
    private static final Integer ID_AKTUALNI_ZASTAVKA = 117;

    @RestService
    MhdWidgetClient restClient;

    @ViewById(R.id.textview_zastavka)
    TextView textViewZastavka;

    @Click({R.id.button_search})
    void updateZastavka() {
        searchAsync(ID_AKTUALNI_ZASTAVKA);
    }

    @Background
    void searchAsync(Integer id) {
        ResponseEntity<AktualniSpoj> entity = restClient.getAktualniSpoj(id);
        afterSearch(entity);
    }

    @UiThread
    void afterSearch(ResponseEntity<AktualniSpoj> entity) {
        if (entity != null && entity.getBody() != null) {
            textViewZastavka.setText(entity.getBody().getZastavka());
        }
    }

}
