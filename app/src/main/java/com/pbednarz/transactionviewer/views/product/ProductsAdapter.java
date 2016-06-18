package com.pbednarz.transactionviewer.views.product;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbednarz.transactionviewer.models.Product;

import java.util.List;

/**
 * Created by pbednarz on 2016-06-18.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private final List<Product> products;
    private final ProductActionInterface productActionInterface;

    public ProductsAdapter(@NonNull List<Product> products, @NonNull ProductActionInterface productActionInterface) {
        this.products = products;
        this.productActionInterface = productActionInterface;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ProductViewHolder(view, productActionInterface);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bindProduct(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface ProductActionInterface {
        void onProductClicked(Product product);
    }
}