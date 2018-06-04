package org.drulabs.popularmovies.ui.details;

import org.drulabs.popularmovies.data.DataHandler;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class DetailsPresenter implements DetailsContract.Presenter{

    private DetailsContract.View view;
    private DataHandler dataHandler;

    /**
     * For keeping all disposables together
     */
    private CompositeDisposable disposables;

    @Inject
    DetailsPresenter(DetailsContract.View view, DataHandler dataHandler){
        this.view = view;
        this.dataHandler = dataHandler;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void start(int movieId) {

    }

    @Override
    public void favoriteTapped(boolean isFavorite) {

    }

    @Override
    public void destroy() {
        disposables.dispose();
    }
}
