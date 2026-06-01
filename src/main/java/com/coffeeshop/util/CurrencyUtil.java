package com.coffeeshop.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtil {
    private static final NumberFormat RUPIAH = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    private CurrencyUtil() {
    }

    public static String format(double value) {
        return RUPIAH.format(value);
    }
}
