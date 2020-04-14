package com.mec.ip.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Math {
    public static double round(double value, int places) {
        if (Double.isNaN(value)) return 0;
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
