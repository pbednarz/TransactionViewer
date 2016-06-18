package com.pbednarz.transactionviewer.views.product.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pbednarz.transactionviewer.models.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(android.R.id.text1)
    TextView transactionCurrencyAmountTv;
    @BindView(android.R.id.text2)
    TextView transactionGBPAmountTv;

    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTransaction(@NonNull Transaction transaction) {
        transactionCurrencyAmountTv.setText(transaction.getAmount());
    }
}