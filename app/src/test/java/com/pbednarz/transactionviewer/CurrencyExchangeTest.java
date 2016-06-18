package com.pbednarz.transactionviewer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pbednarz.transactionviewer.models.Rate;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyConverter;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyConverterGraph;
import com.pbednarz.transactionviewer.providers.exchange.ExchangeRateUndefinedException;
import com.pbednarz.transactionviewer.providers.exchange.GBPCurrencyConverter;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class CurrencyExchangeTest {
    private static final double EQUALS_DELTA = 0.000000000000000000001;
    private final String EUR = "EUR";
    private final String GBP = "GBP";
    private final String PLN = "PLN";
    private final String USD = "USD";
    String RATES_JSON = "[{\"from\":\"AUD\",\"rate\":\"0.5\",\"to\":\"USD\"},{\"from\":\"USD\",\"rate\":\"0.5\",\"to\":\"EUR\"},{\"from\":\"EUR\",\"rate\":\"0.5\",\"to\":\"GBP\"},{\"from\":\"GBP\",\"rate\":\"2\",\"to\":\"EUR\"},{\"from\":\"EUR\",\"rate\":\"2\",\"to\":\"USD\"},{\"from\":\"USD\",\"rate\":\"2\",\"to\":\"AUD\"}]";

    @Test
    public void simpleTest() {
        List<Rate> rates = new Gson().fromJson(RATES_JSON, new TypeToken<ArrayList<Rate>>() {
        }.getType());
        CurrencyConverter ccv = new GBPCurrencyConverter(rates, new CurrencyConverterGraph());

        BigDecimal amount = new BigDecimal(25.0);
        BigDecimal rate = new BigDecimal(0.5);
        try {
            BigDecimal zbr = ccv.convertCurrency(amount, EUR);
            assertEquals(rate.multiply(amount).doubleValue(), zbr.doubleValue(), EQUALS_DELTA);
        } catch (ExchangeRateUndefinedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void undefinedCurrencyTest() {
        List<Rate> rates = new Gson().fromJson(RATES_JSON, new TypeToken<ArrayList<Rate>>() {
        }.getType());
        CurrencyConverter ccv = new GBPCurrencyConverter(rates, new CurrencyConverterGraph());
        try {
            ccv.convertCurrency(BigDecimal.ONE, PLN);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void differentPathTest() {
        List<Rate> rates = new Gson().fromJson(RATES_JSON, new TypeToken<ArrayList<Rate>>() {
        }.getType());
        CurrencyConverter ccv = new GBPCurrencyConverter(rates, new CurrencyConverterGraph());

        BigDecimal amount = new BigDecimal(25.0);
        BigDecimal rate = BigDecimal.ONE;
        try {
            BigDecimal zbr = ccv.convertCurrency(amount, USD);
            assertEquals(rate.multiply(amount).doubleValue(), zbr.doubleValue(), EQUALS_DELTA);
        } catch (ExchangeRateUndefinedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convertToSameCurrencyTest() {
        Rate rate = new Rate(USD, BigDecimal.TEN, GBP);
        CurrencyConverter ccv = new GBPCurrencyConverter(Collections.singletonList(rate), new CurrencyConverterGraph());
        BigDecimal amount = new BigDecimal(25.0);
        try {
            BigDecimal zbr = ccv.convertCurrency(amount, GBP);
            assertEquals(amount.doubleValue(), zbr.doubleValue(), EQUALS_DELTA);
        } catch (ExchangeRateUndefinedException e) {
            e.printStackTrace();
        }
    }
}