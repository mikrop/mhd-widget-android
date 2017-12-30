package cz.mikropsoft.android.mhdwidget;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.androidannotations.annotations.EFragment;

@EFragment
public class MhdPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("MhdPreferences");
        addPreferencesFromResource(R.xml.preferences);

        String preferenceAboutSummary = getResources().getString(R.string.pref_about_summary, BuildConfig.VERSION_NAME);
        Preference aboutPreference = findPreference("preference_about_key");
        aboutPreference.setSummary(preferenceAboutSummary);
    }

}