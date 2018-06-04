package org.drulabs.popularmovies.data;

import android.content.Context;

import org.drulabs.popularmovies.data.models.Movie;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class AppDataHandler implements DataHandler {

    private Context context;

    @Inject
    public AppDataHandler(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Movie> fetchPopularMovies(int pageNumber) {
        return null;
    }

    @Override
    public Observable<Movie> fetchTopRatedMovies(int pageNumber) {
        return null;
    }

    @Override
    public Single<Movie> fetchMovieDetails(int movieId) {
        return null;
    }
}
