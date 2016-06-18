package com.pbednarz.transactionviewer.views.product.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pbednarz.transactionviewer.R;
import com.pbednarz.transactionviewer.models.Transaction;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyConverter;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyFormatter;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(android.R.id.text1) TextView transactionCurrencyAmountTv;
    @BindView(android.R.id.text2) TextView transactionGBPAmountTv;
    @Inject CurrencyConverter currencyConverter;
    @BindString(R.string.exchange_error_formatter) String exchangeErrorFormatter;

    public TransactionViewHolder(@NonNull View itemView, CurrencyConverter currencyConverter) {
        super(itemView);
        this.currencyConverter = currencyConverter;
        ButterKnife.bind(this, itemView);
    }

    public void bindTransaction(@NonNull Transaction transaction) {
        BigDecimal transactionAmount = transaction.getAmount();
        String transactionCurrency = transaction.getCurrency();
        transactionCurrencyAmountTv.setText(CurrencyFormatter.format(transactionAmount.toPlainString(), transactionCurrency));
        try {
            String gbpTransactionAmount = currencyConverter.convertCurrency(transactionAmount, transactionCurrency).toPlainString();
            transactionGBPAmountTv.setText(CurrencyFormatter.format(gbpTransactionAmount, CurrencyFormatter.GBP_CURRENCY));
        } catch (Exception e) {
            transactionGBPAmountTv.setText(String.format(exchangeErrorFormatter, transactionCurrency));
            Timber.e(e, "convertCurrency error");
        }

    }
}