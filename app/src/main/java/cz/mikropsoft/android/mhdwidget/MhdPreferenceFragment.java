package cz.mikropsoft.android.mhdwidget;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.androidannotations.annotations.EFragment;

@EFragment
public class MhdPreferenceFragment extends PreferenceFragment {

    public static MhdPreferenceFragment_ newInstance() {
        return new MhdPreferenceFragment_();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("MhdPreferences");
        addPreferencesFromResource(R.xml.preferences);

        Resources resources = getResources();
        String preferenceAboutSummary = resources.getString(R.string.preference_about_summary, BuildConfig.VERSION_NAME);
        Preference aboutPreference = findPreference("preference_about_key");
        aboutPreference.setSummary(preferenceAboutSummary);
    }

}