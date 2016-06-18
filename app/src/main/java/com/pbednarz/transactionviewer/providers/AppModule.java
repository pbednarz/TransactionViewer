package com.pbednarz.transactionviewer.providers;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pbednarz.transactionviewer.AppController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
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

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }
}