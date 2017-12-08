package cz.mikropsoft.android.mhdwidget;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface MhdPreferences {

    @DefaultString(value = "1", keyRes = R.string.preference_zastavka_id_key)
    String zastavkaId();

}
