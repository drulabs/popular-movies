package org.drulabs.popularmovies.ui.home;

import org.drulabs.popularmovies.data.DataHandler;
import org.drulabs.popularmovies.data.models.Movie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View view;
    private final DataHandler dataHandler;

    /**
     * For keeping all disposables together
     */
    private final CompositeDisposable disposables;

    /**
     * Keeps track of page number of movies
     */
    private int pageNumber = 0;

    @Inject
    HomePresenter(HomeContract.View view, DataHandler dataHandler) {
        this.view = view;
        this.dataHandler = dataHandler;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void start() {
        fetchPopularMovies(false);
    }

    @Override
    public void onMovieTapped(Movie movie) {
        this.view.navigateToMovieDetails(movie);
    }

    @Override
    public void fetchPopularMovies(boolean loadNextBatch) {
        if (!loadNextBatch) {
            pageNumber = 1;
        }
        view.showLoading();
        dataHandler.fetchPopularMovies(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNewMovieObserver(loadNextBatch));
    }

    @Override
    public void fetchTopRatedMovies(boolean loadNextBatch) {
        if (!loadNextBatch) {
            pageNumber = 1;
        }
        view.showLoading();
        dataHandler.fetchTopRatedMovies(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getNewMovieObserver(loadNextBatch));
    }


    @Override
    public void destroy() {
        disposables.dispose();
    }

    private Observer<List<Movie>> getNewMovieObserver(boolean loadNextBatch) {
        return new Observer<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<Movie> movies) {
                if(loadNextBatch){
                    view.appendMovies(movies);
                } else {
                    view.reload(movies);
                }
            }

            @Override
            public void onError(Throwable e) {
                view.onError();
                view.hideLoading();
            }

            @Override
            public void onComplete() {
                view.hideLoading();
                pageNumber++;
            }
        };
    }
}
