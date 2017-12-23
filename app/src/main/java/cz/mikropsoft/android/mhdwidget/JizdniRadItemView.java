package cz.mikropsoft.android.mhdwidget;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.Objects;
import java.util.stream.Collectors;

import cz.mikropsoft.android.mhdwidget.interfaces.MhdRestClient;
import cz.mikropsoft.android.mhdwidget.model.JizdniRad;

@EViewGroup(R.layout.jizdni_rad_list_item)
public class JizdniRadItemView extends LinearLayout {

    private static final String TAG = JizdniRadItemView.class.getName();

    @ViewById
    TextView textViewHodina;
    @ViewById
    TextView textViewMinuty;

    @RestService
    MhdRestClient restClient;

    public JizdniRadItemView(Context context) {
        super(context);
    }

    public void bind(final JizdniRad jizdniRad) {
        if (jizdniRad != null) {
            textViewHodina.setText(Objects.toString(jizdniRad.getHodina().getHourOfDay()));
            String minuty = jizdniRad.getMinuty().stream()
                    .map(odjezd -> Objects.toString(odjezd.getMinuteOfHour()))
                    .collect(Collectors.joining(","));
            textViewMinuty.setText(minuty);
        }
    }

}
