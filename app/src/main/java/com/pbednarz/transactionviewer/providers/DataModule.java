package com.pbednarz.transactionviewer.providers;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pbednarz.transactionviewer.R;
import com.pbednarz.transactionviewer.models.Product;
import com.pbednarz.transactionviewer.models.Rate;
import com.pbednarz.transactionviewer.models.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Module
public class DataModule {

    @Provides
    @Singleton
    public JSONResourceReader provideJsonResourceReader(Gson gson, Application application) {
        return new JSONResourceReader(gson, application.getResources());
    }

    @Provides
    public List<Product> provideProducts(List<Transaction> transactions) {
        if (transactions == null) {
            return new ArrayList<>();
        }
        Map<String, List<Transaction>> productsMap = new HashMap<>(transactions.size());
        for (Transaction transaction : transactions) {
            String sku = transaction.getSku();
            List<Transaction> transactionsForProduct = productsMap.get(sku);
            if (transactionsForProduct == null) {
                transactionsForProduct = new ArrayList<>();
            }
            transactionsForProduct.add(transaction);
            productsMap.put(sku, transactionsForProduct);
        }
        List<Product> products = new ArrayList<>(productsMap.size());
        for (Map.Entry<String, List<Transaction>> productEntry : productsMap.entrySet()) {
            products.add(new Product(productEntry.getKey(), productEntry.getValue()));
        }
        return products;
    }

    @Provides
    public List<Rate> provideRates(JSONResourceReader jsonReader) {
        return jsonReader.readFromRaw(R.raw.rates, new TypeToken<ArrayList<Rate>>() {
        }.getType());
    }

    @Provides
    public List<Transaction> provideTransactions(JSONResourceReader jsonReader) {
        return jsonReader.readFromRaw(R.raw.transactions, new TypeToken<ArrayList<Transaction>>() {
        }.getType());
    }
}