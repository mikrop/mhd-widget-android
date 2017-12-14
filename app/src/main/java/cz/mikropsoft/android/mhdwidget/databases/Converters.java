package cz.mikropsoft.android.mhdwidget.databases;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

public class Converters {

    @TypeConverter
    public static LocalTime fromTimestamp(Long value) {
        return value == null ? null : new LocalTime(Long.valueOf(value), DateTimeZone.getDefault());
    }

    @TypeConverter
    public static Long localTimeToTimestamp(LocalTime localTime) {
        return localTime == null ? null : localTime.toDateTimeToday().getMillis();
    }

}
