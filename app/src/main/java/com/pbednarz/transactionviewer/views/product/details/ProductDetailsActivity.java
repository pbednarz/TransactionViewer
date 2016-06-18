package com.pbednarz.transactionviewer.views.product.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.pbednarz.transactionviewer.AppController;
import com.pbednarz.transactionviewer.R;
import com.pbednarz.transactionviewer.models.Product;
import com.pbednarz.transactionviewer.models.Transaction;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyConverter;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyFormatter;
import com.pbednarz.transactionviewer.providers.exchange.ExchangeRateUndefinedException;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class ProductDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_PRODUCT = "ProductDetailsActivity.EXTRA_PRODUCT";
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.total) TextView totalAmountTv;
    @Inject CurrencyConverter currencyConverter;

    public static Intent getStartIntent(@NonNull Context context, @NonNull Product product) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra(EXTRA_PRODUCT, product);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Product product = getIntent().getParcelableExtra(EXTRA_PRODUCT);
        if (product == null) {
            finish();
            return;
        }
        ButterKnife.bind(this);
        AppController.getComponent(this).inject(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(product.getSku());
        }
        ProductsDetailsAdapter detailsAdapter = new ProductsDetailsAdapter(product.getTransactions(), currencyConverter);
        initUI(detailsAdapter);
        calculateTotalAmount(product.getTransactions());
    }

    private void calculateTotalAmount(List<Transaction> transactions) {
        BigDecimal total = new BigDecimal(0);
        for (Transaction transaction : transactions) {
            try {
                total = total.add(currencyConverter.convertCurrency(transaction.getAmount(), transaction.getCurrency()));
            } catch (ExchangeRateUndefinedException e) {
                e.printStackTrace();
            }
        }
        String totalStringFormatted = CurrencyFormatter.UK_FORMATTER.format(total);
        totalAmountTv.setText(getString(R.string.total_sum_formatter, totalStringFormatted));
    }

    private void initUI(RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityCompat.finishAfterTransition(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}