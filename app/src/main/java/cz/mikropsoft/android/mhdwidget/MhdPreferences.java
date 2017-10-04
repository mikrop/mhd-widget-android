package cz.mikropsoft.android.mhdwidget;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface MhdPreferences {

    @DefaultInt(value = 1, keyRes = R.string.preference_zastavka_id_key)
    int zastavkaId();

}
