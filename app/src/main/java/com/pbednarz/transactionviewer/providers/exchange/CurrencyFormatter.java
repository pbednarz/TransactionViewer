package com.pbednarz.transactionviewer.providers.exchange;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class CurrencyFormatter {

    public static final DecimalFormat UK_FORMATTER = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.UK);
    public static final DecimalFormat DEFAULT_FORMATTER = (DecimalFormat) DecimalFormat.getCurrencyInstance();
    public static final String GBP_CURRENCY = "GBP";

    private CurrencyFormatter() {
    }

    public static String formatUK(@NonNull BigDecimal amount) {
        return UK_FORMATTER.format(amount);
    }

    public static String format(@NonNull BigDecimal amount, @NonNull String currencyCode) {
        if (GBP_CURRENCY.equals(currencyCode)) {
            return formatUK(amount);
        }
        java.util.Currency currency = getCurrencyForCode(currencyCode.toUpperCase());
        if (currency == null) {
            return currencyCode + amount;
        }
        DEFAULT_FORMATTER.setCurrency(currency);
        return DEFAULT_FORMATTER.format(amount);
    }

    @Nullable
    public static java.util.Currency getCurrencyForCode(@NonNull String code) {
        try {
            return java.util.Currency.getInstance(code);
        } catch (Exception localNotSupportedException) {
            return null;
        }
    }
}
