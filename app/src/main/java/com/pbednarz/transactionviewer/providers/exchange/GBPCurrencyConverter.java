package com.pbednarz.transactionviewer.providers.exchange;

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

    public GBPCurrencyConverter(List<Rate> rates, CurrencyConverterGraph currencyConverterGraph) {
        this.gbpRates = new HashMap<>();
        this.currencyConverterGraph = currencyConverterGraph;
        for (Rate rate : rates) {
            if (GBP_CURRENCY.equals(rate.getTo())) {
                gbpRates.put(rate.getFrom(), new BigDecimal(rate.getRate()));
            }
        }
        for (Rate rate : rates) {
            currencyConverterGraph.setExchangeRate(rate.getFrom(), rate.getTo(), new BigDecimal(rate.getRate()));
        }
    }

    @Override
    public BigDecimal convertCurrency(String inValue, String currencyFrom) throws ArithmeticException, ExchangeRateUndefinedException {
        return convertCurrency(new BigDecimal(inValue), currencyFrom);
    }

    @Override
    public BigDecimal convertCurrency(BigDecimal inValue, String currencyFrom) throws ArithmeticException, ExchangeRateUndefinedException {
        if (GBP_CURRENCY.equals(currencyFrom)) {
            return inValue;
        }

        BigDecimal gbp_currency_rate = getKnownRate(currencyFrom);
        if (gbp_currency_rate != null) {
            BigDecimal gbp = inValue.multiply(gbp_currency_rate);
            gbp = gbp.setScale(5, BigDecimal.ROUND_HALF_UP);
            return gbp;
        }

        return currencyConverterGraph.convertCurrency(currencyFrom, GBP_CURRENCY, inValue);
    }

    public BigDecimal getKnownRate(String currencyFrom) {
        BigDecimal gbpRate = gbpRates.get(currencyFrom);
        if (gbpRate != null) {
            return gbpRate;
        }

        return null;
    }
}