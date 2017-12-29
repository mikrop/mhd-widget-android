package cz.mikropsoft.android.mhdwidget.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import cz.mikropsoft.android.mhdwidget.R;

/**
 * Výčtová hodnota dopravních prostředků.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Prostredek {
    AUTOBUS(R.drawable.ic_directions_bus),
    TROLEJBUS(R.drawable.ic_directions_bus),
    TRAMVAJ(R.drawable.ic_tram)
    ;

    int resid;

    private Prostredek(int resid) {
        this.resid = resid;
    }

    /**
     * ID obrázku z res.
     *
     * @return ID obrázku
     */
    public int getResid() {
        return resid;
    }

}
