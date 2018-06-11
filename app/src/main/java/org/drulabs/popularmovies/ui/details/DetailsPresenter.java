package org.drulabs.popularmovies.ui.details;

import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.DataHandler;
import org.drulabs.popularmovies.data.models.Movie;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsPresenter implements DetailsContract.Presenter {

    private final DetailsContract.View view;
    private final DataHandler dataHandler;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat
            ("yyyy-MM-dd", Locale.getDefault());

    /**
     * For keeping all disposables together
     */
    private final CompositeDisposable disposables;

    @Inject
    DetailsPresenter(DetailsContract.View view, DataHandler dataHandler) {
        this.view = view;
        this.dataHandler = dataHandler;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void start(long movieId) {
        view.showLoading();
        dataHandler.fetchMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(Movie movie) {
                        if (movie != null) {
                            view.loadPoster(AppConstants.TMDB_POSTER_BASE + movie.getPosterPath());
                            view.loadBackdrop(AppConstants.TMDB_POSTER_BASE + movie
                                    .getBackdropPath());
                            view.loadRuntime(String.valueOf(movie.getRuntime()));
                            view.loadRating(String.valueOf(movie.getVoteAverage()));
                            view.loadSummary(movie.getOverview());
                            view.loadTitle(movie.getTitle());
                            view.loadReleaseDate(movie.getReleaseDate());
                        }
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void favoriteTapped(boolean isFavorite) {
        // TODO implement it in popular movies phase 2
    }

    @Override
    public void destroy() {
        disposables.dispose();
    }
}
