package cz.mikropsoft.android.mhdwidget;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.junit.Assert;

import java.util.Objects;

import cz.mikropsoft.android.mhdwidget.model.SpojeZaHodinu;

@EViewGroup(R.layout.jizdni_rad_list_item)
public class JizdniRadItemView extends LinearLayout {

    private static final String TAG = JizdniRadItemView.class.getName();

    @ViewById
    TextView textViewHodinaDne;
    @ViewById
    TextView textViewMinuty;

    public JizdniRadItemView(Context context) {
        super(context);
    }

    public void bind(SpojeZaHodinu spojeZaHodinu) {
        Assert.assertNotNull(spojeZaHodinu);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        spojeZaHodinu.getSpoje()
                .forEach(spoj -> {

                    String minutaOdjezdu = Objects.toString(spoj.getOdjezd().getMinuteOfHour());
                    int start = builder.length();
                    builder.append(minutaOdjezdu);
                    if (spoj.isAktualni()) {
                        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.accent, getContext().getTheme())), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    builder.append(" ");
                });
        textViewHodinaDne.setText(Objects.toString(spojeZaHodinu.getHodinaDne()));
        textViewMinuty.setText(builder, TextView.BufferType.SPANNABLE);
    }

}
