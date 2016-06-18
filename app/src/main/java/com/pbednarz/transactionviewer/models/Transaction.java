package com.pbednarz.transactionviewer.models;

import lombok.Value;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Value
public class Transaction {
    private final String amount;
    private final String sku;
    private final String currency;
}
