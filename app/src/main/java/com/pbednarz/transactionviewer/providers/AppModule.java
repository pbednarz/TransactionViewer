package com.pbednarz.transactionviewer.providers;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pbednarz.transactionviewer.AppController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Module(includes = {DataModule.class})
public class AppModule {

    private final AppController app;

    public AppModule(AppController app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .serializeNulls()
                .create();
    }
}