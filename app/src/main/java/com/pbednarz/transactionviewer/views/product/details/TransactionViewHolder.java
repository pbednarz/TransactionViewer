package com.pbednarz.transactionviewer.views.product.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pbednarz.transactionviewer.models.Transaction;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyConverter;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyFormatter;
import com.pbednarz.transactionviewer.providers.exchange.ExchangeRateUndefinedException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(android.R.id.text1) TextView transactionCurrencyAmountTv;
    @BindView(android.R.id.text2) TextView transactionGBPAmountTv;
    @Inject CurrencyConverter currencyConverter;

    public TransactionViewHolder(@NonNull View itemView, CurrencyConverter currencyConverter) {
        super(itemView);
        this.currencyConverter = currencyConverter;
        ButterKnife.bind(this, itemView);
    }

    public void bindTransaction(@NonNull Transaction transaction) {
        String transactionAmount = transaction.getAmount();
        transactionCurrencyAmountTv.setText(CurrencyFormatter.format(transactionAmount, transaction.getCurrency()));
        try {
            String gbpTransactionAmount = currencyConverter.convertCurrency(transactionAmount, transaction.getCurrency()).toPlainString();
            transactionGBPAmountTv.setText(CurrencyFormatter.format(gbpTransactionAmount, CurrencyFormatter.GBP_CURRENCY));
        } catch (ExchangeRateUndefinedException e) {
            transactionGBPAmountTv.setText(e.getLocalizedMessage());
        }

    }
}