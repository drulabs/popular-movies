package org.drulabs.popularmovies.ui.home;

import org.drulabs.popularmovies.data.DataHandler;
import org.drulabs.popularmovies.data.models.Movie;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private DataHandler dataHandler;

    /**
     * For keeping all disposables together
     */
    private CompositeDisposable disposables;

    /**
     * Keeps track of page number of popular movies
     */
    private int popularMoviePage = 1;

    /**
     * Keeps track of page number of top rated movies
     */
    private int topRatedMoviePage = 1;

    @Inject
    HomePresenter(HomeContract.View view, DataHandler dataHandler) {
        this.view = view;
        this.dataHandler = dataHandler;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void start() {

    }

    @Override
    public void onMovieTapped(Movie movie) {

    }

    @Override
    public void fetchPopularMovies() {
        view.showLoading();

        dataHandler.fetchPopularMovies(popularMoviePage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Movie movie) {
                        view.appendMovie(movie);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError();
                    }

                    @Override
                    public void onComplete() {
                        popularMoviePage++;
                    }
                });
    }

    @Override
    public void fetchTopRatedMovies() {
        dataHandler.fetchTopRatedMovies(topRatedMoviePage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Movie movie) {
                        view.appendMovie(movie);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError();
                        view.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        topRatedMoviePage++;
                        view.hideLoading();
                    }
                });
    }


    @Override
    public void destroy() {
        disposables.dispose();
    }
}
