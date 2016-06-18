package com.pbednarz.transactionviewer;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pbednarz.transactionviewer.providers.AppComponent;
import com.pbednarz.transactionviewer.providers.AppModule;
import com.pbednarz.transactionviewer.providers.DaggerAppComponent;

/**
 * Created by pbednarz on 2016-06-18.
 */
public class AppController extends Application {
    private static AppController instance;
    private AppComponent component;

    public static AppController getInstance() {
        return instance;
    }

    @NonNull
    public static AppComponent getComponent(@NonNull Context context) {
        return ((AppController) context.getApplicationContext()).getComponent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = buildGraph();
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