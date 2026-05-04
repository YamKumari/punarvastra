package com.punarvastra.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Nepali Rupee display formatting.
 */
public final class PriceUtil {

    private static final DecimalFormat FORMAT;

    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.UK);
        sym.setGroupingSeparator(',');
        FORMAT = new DecimalFormat("#,##0.00", sym);
    }

    private PriceUtil() {
    }

    /**
     * Formats an amount as {@code Rs. 1,234.50}.
     *
     * @param amount nullable amount
     * @return prefixed formatted string
     */
    public static String formatNpr(BigDecimal amount) {
        if (amount == null) {
            return "Rs. 0.00";
        }
        return "Rs. " + FORMAT.format(amount);
    }

    /**
     * Parses a positive decimal from form input.
     *
     * @param raw trimmed string
     * @return value or null if invalid
     */
    public static BigDecimal parsePositive(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            BigDecimal v = new BigDecimal(raw.trim());
            if (v.compareTo(BigDecimal.ZERO) < 0) {
                return null;
            }
            return v;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
