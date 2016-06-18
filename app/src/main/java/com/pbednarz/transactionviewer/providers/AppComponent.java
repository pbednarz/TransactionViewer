package com.pbednarz.transactionviewer.providers;

import com.pbednarz.transactionviewer.AppController;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
        })

public interface AppComponent {
    void inject(AppController app);
}