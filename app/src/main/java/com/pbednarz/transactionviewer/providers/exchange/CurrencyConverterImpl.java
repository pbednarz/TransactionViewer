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

public class CurrencyConverterImpl implements CurrencyConverter {
    private final String originCurrencyCode;
    private final CurrencyConverterGraph currencyConverterGraph;
    private final Map<String, BigDecimal> ratesCache = new HashMap<>();

    public CurrencyConverterImpl(@NonNull String originCurrencyCode, @NonNull List<Rate> rates, @NonNull CurrencyConverterGraph currencyConverterGraph) {
        this.originCurrencyCode = originCurrencyCode;
        this.currencyConverterGraph = currencyConverterGraph;
        for (Rate rate : rates) {
            if (originCurrencyCode.equals(rate.getTo())) {
                ratesCache.put(rate.getFrom(), rate.getRate());
            }
        }
        for (Rate rate : rates) {
            currencyConverterGraph.setExchangeRate(rate);
        }
    }

    @Override
    public BigDecimal convertCurrency(@NonNull BigDecimal inValue, @NonNull String currencyFrom) throws ArithmeticException, ExchangeRateUndefinedException {
        if (originCurrencyCode.equals(currencyFrom)) {
            return inValue;
        }

        BigDecimal rate = getRate(currencyFrom);
        BigDecimal outValue = inValue.multiply(rate);
        outValue = outValue.setScale(2, BigDecimal.ROUND_HALF_UP);
        return outValue;
    }

    private BigDecimal getRate(@NonNull String currencyFrom) throws ExchangeRateUndefinedException {
        BigDecimal rate = ratesCache.get(currencyFrom);
        if (rate == null) {
            rate = currencyConverterGraph.getCurrencyRate(currencyFrom, originCurrencyCode);
            ratesCache.put(currencyFrom, rate);
        }
        return rate;
    }
}