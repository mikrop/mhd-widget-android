package cz.mikropsoft.android.mhdwidget;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import org.androidannotations.annotations.EActivity;

@EActivity
public class MhdPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new MhdPreferenceFragment_())
                .commit();
    }

}
