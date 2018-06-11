package org.drulabs.popularmovies.data;

import android.content.Context;

import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.data.models.MovieResult;
import org.drulabs.popularmovies.data.remote.TMDBApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class AppDataHandler implements DataHandler {

    private final TMDBApi tmdbApi;

    @Inject
    AppDataHandler(Context context, TMDBApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @Override
    public Observable<List<Movie>> fetchPopularMovies(int pageNumber) {
        return tmdbApi.getPopularMovies(pageNumber)
                .flatMap((Function<MovieResult, ObservableSource<List<Movie>>>) movieResult ->
                        Observable.just(movieResult.getMovies()));
    }

    @Override
    public Observable<List<Movie>> fetchTopRatedMovies(int pageNumber) {
        return tmdbApi.getTopRatedMovies(pageNumber)
                .flatMap((Function<MovieResult, ObservableSource<List<Movie>>>) movieResult ->
                        Observable.just(movieResult.getMovies()));
    }

    @Override
    public Single<Movie> fetchMovieDetails(long movieId) {
        return tmdbApi.getMovieDetails(movieId);
    }
}
