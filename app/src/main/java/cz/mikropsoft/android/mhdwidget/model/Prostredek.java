package cz.mikropsoft.android.mhdwidget.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Výčtová hodnota dopravních prostředků.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Prostredek {
    AUTOBUS,
    TROLEJBUS,
    TRAMVAJ
    ;
}
