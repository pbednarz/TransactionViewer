package com.pbednarz.transactionviewer.views.product.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbednarz.transactionviewer.models.Transaction;
import com.pbednarz.transactionviewer.providers.exchange.CurrencyConverter;

import java.util.List;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class ProductsDetailsAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private final List<Transaction> transactions;
    private final CurrencyConverter currencyConverter;

    public ProductsDetailsAdapter(@NonNull List<Transaction> transactions, @NonNull CurrencyConverter currencyConverter) {
        this.transactions = transactions;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        return new TransactionViewHolder(view, currencyConverter);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.bindTransaction(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}