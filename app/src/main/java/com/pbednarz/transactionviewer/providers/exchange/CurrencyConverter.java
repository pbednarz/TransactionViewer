package com.pbednarz.transactionviewer.providers.exchange;

import java.math.BigDecimal;

/**
 * Created by pbednarz on 2016-06-18.
 */

public interface CurrencyConverter {
    BigDecimal convertCurrency(BigDecimal inValue, String currencyFrom) throws ArithmeticException, ExchangeRateUndefinedException;
}