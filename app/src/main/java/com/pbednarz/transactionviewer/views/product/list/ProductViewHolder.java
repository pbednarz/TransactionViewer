package com.pbednarz.transactionviewer.views.product.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pbednarz.transactionviewer.R;
import com.pbednarz.transactionviewer.models.Product;
import com.pbednarz.transactionviewer.models.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ProductsAdapter.ProductActionInterface productActionInterface;
    @BindView(android.R.id.text1) TextView productNameTv;
    @BindView(android.R.id.text2) TextView productTransactionTv;
    private Product product;

    public ProductViewHolder(@NonNull View itemView, @NonNull ProductsAdapter.ProductActionInterface productActionInterface) {
        super(itemView);
        this.productActionInterface = productActionInterface;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    public void bindProduct(@NonNull Product product) {
        this.product = product;
        String sku = product.getSku();
        List<Transaction> transactionList = product.getTransactions();
        int transactionsCount = transactionList != null ? transactionList.size() : 0;
        productNameTv.setText(sku);
        String quantityString = itemView.getResources().getQuantityString(R.plurals.transactions_formatter,
                transactionsCount, transactionsCount);
        productTransactionTv.setText(quantityString);
    }

    @Override
    public void onClick(View view) {
        if (product != null) {
            productActionInterface.onProductClicked(product);
        }
    }
}