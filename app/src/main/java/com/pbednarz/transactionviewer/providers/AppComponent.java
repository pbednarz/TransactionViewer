package com.pbednarz.transactionviewer.providers;

import com.pbednarz.transactionviewer.AppController;
import com.pbednarz.transactionviewer.ProductsActivity;

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
    void inject(AppController app);

    void inject(ProductsActivity productsActivity);
}