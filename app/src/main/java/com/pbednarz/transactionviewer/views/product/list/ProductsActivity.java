package com.pbednarz.transactionviewer.views.product.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pbednarz.transactionviewer.AppController;
import com.pbednarz.transactionviewer.R;
import com.pbednarz.transactionviewer.models.Product;
import com.pbednarz.transactionviewer.views.product.details.ProductDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsActivity extends AppCompatActivity implements ProductsAdapter.ProductActionInterface {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @Inject List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        AppController.getComponent(this).inject(this);
        ProductsAdapter productAdapter = new ProductsAdapter(products, this);
        initUI(productAdapter);
    }

    private void initUI(RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProductClicked(Product product) {
        startActivity(ProductDetailsActivity.getStartIntent(this, product));
    }
}