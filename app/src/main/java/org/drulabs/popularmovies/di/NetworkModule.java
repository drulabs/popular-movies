package org.drulabs.popularmovies.di;

import org.drulabs.popularmovies.data.remote.TMDBApi;
import org.drulabs.popularmovies.data.remote.TMDBApiBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    TMDBApi providesTMDBApi() {
        return (new TMDBApiBuilder()).build();
    }
}
