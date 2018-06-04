package org.drulabs.popularmovies.di;

import org.drulabs.popularmovies.data.AppDataHandler;
import org.drulabs.popularmovies.data.DataHandler;
import org.drulabs.popularmovies.ui.details.DetailsContract;
import org.drulabs.popularmovies.ui.details.DetailsPresenter;
import org.drulabs.popularmovies.ui.home.HomeContract;
import org.drulabs.popularmovies.ui.home.HomePresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PresenterModule {

    @Binds
    abstract HomeContract.Presenter bindHomePresenter(HomePresenter homePresenter);

    @Binds
    abstract DetailsContract.Presenter bindDetailsPresenter(DetailsPresenter detailsPresenter);

    @Binds
    abstract DataHandler bindDataHandler(AppDataHandler appDataHandler);

}
