package org.drulabs.popularmovies.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.drulabs.popularmovies.config.AppConstants;
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
                .subscribe(getNewMovieObserver(loadNextBatch, AppConstants.SELECTION_POPULAR_MOVIES));
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
                .subscribe(getNewMovieObserver(loadNextBatch, AppConstants.SELECTION_TOP_RATED_MOVIES));
    }

    @Override
    public void fetchAllFavoriteMovies(@NonNull AppCompatActivity appCompatActivity) {
        HomeViewModel homeViewModel = ViewModelProviders.of(appCompatActivity).get(HomeViewModel
                .class);
//        if (!homeViewModel.getFavoriteMovies().hasActiveObservers()) {
        homeViewModel.getFavoriteMovies().removeObservers(appCompatActivity);
        homeViewModel.getFavoriteMovies().observe(appCompatActivity, movies -> {
            view.reload(movies, AppConstants.SELECTION_FAVORITE_MOVIES);
            view.hideLoading();
        });
//        } else {
//            view.reload(homeViewModel.getFavoriteMovies().getValue(), AppConstants
//                    .SELECTION_FAVORITE_MOVIES);
//            view.hideLoading();
//        }
    }

    @Override
    public void fetchCachedPopularMovies(@NonNull AppCompatActivity appCompatActivity) {
        HomeViewModel homeViewModel = ViewModelProviders.of(appCompatActivity).get(HomeViewModel
                .class);
        homeViewModel.getTopRatedMovies().removeObservers(appCompatActivity);
        homeViewModel.getPopularMovies().observe(appCompatActivity, movies -> {
            view.reload(movies, AppConstants.SELECTION_POPULAR_MOVIES);
            view.hideLoading();
        });
    }

    @Override
    public void fetchCachedTopRatedMovies(@NonNull AppCompatActivity appCompatActivity) {
        HomeViewModel homeViewModel = ViewModelProviders.of(appCompatActivity).get(HomeViewModel
                .class);
        homeViewModel.getTopRatedMovies().removeObservers(appCompatActivity);
        homeViewModel.getTopRatedMovies().observe(appCompatActivity, movies -> {
            view.reload(movies, AppConstants.SELECTION_TOP_RATED_MOVIES);
            view.hideLoading();
        });
    }


    @Override
    public void destroy() {
        disposables.dispose();
    }

    private Observer<List<Movie>> getNewMovieObserver(boolean loadNextBatch, int category) {
        return new Observer<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<Movie> movies) {
                if (loadNextBatch) {
                    view.appendMovies(movies);
                } else {
                    view.reload(movies, category);
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
