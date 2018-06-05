package org.drulabs.popularmovies.ui.details;

import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.DataHandler;
import org.drulabs.popularmovies.data.models.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsPresenter implements DetailsContract.Presenter {

    private DetailsContract.View view;
    private DataHandler dataHandler;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat
            ("yyyy-MM-dd", Locale.getDefault());

    /**
     * For keeping all disposables together
     */
    private CompositeDisposable disposables;

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
                            view.loadRuntime(String.valueOf(movie.getRuntime()));
                            view.loadRating(String.valueOf(movie.getVoteAverage()));
                            view.loadSummary(movie.getOverview());
                            view.loadTitle(movie.getOriginalTitle());
                            try {
                                Date releaseDate = dateFormat.parse(movie.getReleaseDate());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(releaseDate);
                                view.loadYear(String.valueOf(calendar.get(Calendar.YEAR)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
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

    }

    @Override
    public void destroy() {
        disposables.dispose();
    }
}
