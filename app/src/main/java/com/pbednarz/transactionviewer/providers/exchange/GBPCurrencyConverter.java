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
    private final CurrencyConverterGraph currencyConverterGraph;
    private final Map<String, BigDecimal> ratesCache = new HashMap<>();

    public GBPCurrencyConverter(@NonNull List<Rate> rates, @NonNull CurrencyConverterGraph currencyConverterGraph) {
        this.currencyConverterGraph = currencyConverterGraph;
        for (Rate rate : rates) {
            if (GBP_CURRENCY.equals(rate.getTo())) {
                ratesCache.put(rate.getFrom(), rate.getRate());
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

        BigDecimal rate = getRate(currencyFrom);
        BigDecimal gbp = inValue.multiply(rate);
        gbp = gbp.setScale(2, BigDecimal.ROUND_HALF_UP);
        return gbp;
    }

    private BigDecimal getRate(@NonNull String currencyFrom) throws ExchangeRateUndefinedException {
        BigDecimal rate = ratesCache.get(currencyFrom);
        if (rate == null) {
            rate = currencyConverterGraph.getCurrencyRate(currencyFrom, GBP_CURRENCY);
            ratesCache.put(currencyFrom, rate);
        }
        return rate;
    }
}