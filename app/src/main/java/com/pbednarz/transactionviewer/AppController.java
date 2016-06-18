package com.pbednarz.transactionviewer;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pbednarz.transactionviewer.providers.AppComponent;
import com.pbednarz.transactionviewer.providers.AppModule;
import com.pbednarz.transactionviewer.providers.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by pbednarz on 2016-06-18.
 */
public class AppController extends Application {
    private AppComponent component;

    @NonNull
    public static AppComponent getComponent(@NonNull Context context) {
        return ((AppController) context.getApplicationContext()).getComponent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildGraph();
        LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    protected AppComponent buildGraph() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }
}