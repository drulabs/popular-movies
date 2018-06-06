package org.drulabs.popularmovies.application;

import android.app.Application;

import org.drulabs.popularmovies.di.AppComponent;
import org.drulabs.popularmovies.di.AppModule;
import org.drulabs.popularmovies.di.DaggerAppComponent;
import org.drulabs.popularmovies.di.NetworkModule;

public class AppClass extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
