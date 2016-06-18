package com.pbednarz.transactionviewer.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Data
@AllArgsConstructor
public class Product {
    String sku;
    List<Transaction> transactions;
}
