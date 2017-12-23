package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.TypeConverter;

import cz.mikropsoft.android.mhdwidget.model.Prostredek;

public class ProstredekConverter {

    @TypeConverter
    public static Prostredek fromName(String name) {
        return name == null ? null : Prostredek.valueOf(Prostredek.class, name);
    }

    @TypeConverter
    public static String prostredekToName(Prostredek prostredek) {
        return prostredek == null ? null : prostredek.name();
    }

}
