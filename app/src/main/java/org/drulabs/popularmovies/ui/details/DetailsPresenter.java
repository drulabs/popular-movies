package org.drulabs.popularmovies.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.DataHandler;
import org.drulabs.popularmovies.data.local.MovieDatabase;
import org.drulabs.popularmovies.data.models.Movie;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsPresenter implements DetailsContract.Presenter {

    private final DetailsContract.View view;
    private final DataHandler dataHandler;

    private Movie currentMovie;

    private boolean isFavorite = false;

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
                            currentMovie = movie;
                            populateMovieDetails(movie, true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                    }
                });
    }

    private void populateMovieDetails(@NonNull Movie movie, boolean executeAnimations) {
        view.loadPoster(AppConstants.TMDB_POSTER_BASE + movie.getPosterPath());
        view.loadBackdrop(AppConstants.TMDB_POSTER_BASE + movie
                .getBackdropPath());
        view.loadRuntime(String.valueOf(movie.getRuntime()));
        view.loadRating(String.valueOf(movie.getVoteAverage()));
        view.loadSummary(movie.getOverview());
        view.loadTitle(movie.getTitle());
        view.loadReleaseDate(movie.getReleaseDate());
        view.hideLoading();
        if (executeAnimations) {
            view.executeAnimations();
        }
    }

    @Override
    public void favoriteTapped() {
        if (!isFavorite) {
            dataHandler.addFavoriteMovie(currentMovie)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        } else {
            dataHandler.deleteFavoritedMovie(currentMovie)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        }
    }

    @Override
    public void observeCurrentMovie(AppCompatActivity context, long movieId, boolean
            executeAnimations) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(context);
        DetailViewModelFactory factory = new DetailViewModelFactory(movieDatabase, movieId);
        final DetailViewModel detailViewModel = ViewModelProviders.of(context, factory).get
                (DetailViewModel.class);
        detailViewModel.getMovieLiveData().observe(context, movie -> {
            if (movie != null) {
                currentMovie = movie;
                if (!movie.isFavorite()) {
                    view.unmarkFavorite();
                    isFavorite = false;
                } else {
                    view.markFavorite();
                    isFavorite = true;
                }
                populateMovieDetails(movie, executeAnimations);
            }
        });
    }

    @Override
    public void destroy() {
        disposables.dispose();
    }
}
