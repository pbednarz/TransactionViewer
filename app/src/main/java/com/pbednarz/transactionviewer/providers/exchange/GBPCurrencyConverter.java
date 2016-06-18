package com.pbednarz.transactionviewer.providers.exchange;

import android.support.annotation.NonNull;

import com.pbednarz.transactionviewer.models.Rate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class GBPCurrencyConverter implements CurrencyConverter {
    private final String GBP_CURRENCY = "GBP";
    private final Map<String, BigDecimal> gbpRates;
    private final CurrencyConverterGraph currencyConverterGraph;

    public GBPCurrencyConverter(@NonNull List<Rate> rates, @NonNull CurrencyConverterGraph currencyConverterGraph) {
        this.gbpRates = new HashMap<>();
        this.currencyConverterGraph = currencyConverterGraph;
        for (Rate rate : rates) {
            if (GBP_CURRENCY.equals(rate.getTo())) {
                gbpRates.put(rate.getFrom(), rate.getRate());
            }
        }
        for (Rate rate : rates) {
            currencyConverterGraph.setExchangeRate(rate);
        }
    }

    @Override
    public BigDecimal convertCurrency(@NonNull BigDecimal inValue, @NonNull String currencyFrom) throws ArithmeticException, ExchangeRateUndefinedException {
        if (GBP_CURRENCY.equals(currencyFrom)) {
            return inValue;
        }

        BigDecimal gbp_currency_rate = getKnownRate(currencyFrom);
        if (gbp_currency_rate != null) {
            BigDecimal gbp = inValue.multiply(gbp_currency_rate);
            gbp = gbp.setScale(2, BigDecimal.ROUND_HALF_UP);
            return gbp;
        }

        return currencyConverterGraph.convertCurrency(currencyFrom, GBP_CURRENCY, inValue);
    }

    public BigDecimal getKnownRate(@NonNull String currencyFrom) {
        BigDecimal gbpRate = gbpRates.get(currencyFrom);
        if (gbpRate != null) {
            return gbpRate;
        }

        return null;
    }
}