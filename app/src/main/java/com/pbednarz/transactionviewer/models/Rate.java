package com.pbednarz.transactionviewer.models;

import lombok.Value;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Value
public class Rate {
    private final String from;
    private final String rate;
    private final String to;
}
