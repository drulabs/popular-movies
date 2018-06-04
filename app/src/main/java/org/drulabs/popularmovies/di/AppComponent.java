package org.drulabs.popularmovies.di;

import android.app.Application;
import android.content.Context;

import org.drulabs.popularmovies.data.remote.TMDBApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    TMDBApi getTMDBApi();

    Context getContext();

    Application getApplication();

}
