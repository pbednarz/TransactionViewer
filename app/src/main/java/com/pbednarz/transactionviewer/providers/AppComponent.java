package com.pbednarz.transactionviewer.providers;

import com.pbednarz.transactionviewer.views.product.details.ProductDetailsActivity;
import com.pbednarz.transactionviewer.views.product.list.ProductsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class
        })

public interface AppComponent {

    void inject(ProductsActivity productsActivity);

    void inject(ProductDetailsActivity productDetailsActivity);
}