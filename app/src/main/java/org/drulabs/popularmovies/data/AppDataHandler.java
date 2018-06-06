package org.drulabs.popularmovies.data;

import android.content.Context;

import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.data.models.MovieResult;
import org.drulabs.popularmovies.data.remote.TMDBApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AppDataHandler implements DataHandler {

    private final TMDBApi tmdbApi;

    @Inject
    AppDataHandler(Context context, TMDBApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    @Override
    public Single<List<Movie>> fetchPopularMovies(int pageNumber) {
        return Single.create(e -> tmdbApi.getPopularMovies(pageNumber)
                .subscribe(getSingleMovieObserver(e)));
    }

    @Override
    public Single<List<Movie>> fetchTopRatedMovies(int pageNumber) {
        return Single.create(e -> tmdbApi.getTopRatedMovies(pageNumber)
                .subscribe(getSingleMovieObserver(e)));
    }

    @Override
    public Single<Movie> fetchMovieDetails(long movieId) {
        return tmdbApi.getMovieDetails(movieId);
    }

    private SingleObserver<MovieResult> getSingleMovieObserver(SingleEmitter<List<Movie>> emitter) {
        return new SingleObserver<MovieResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                // Ignore
            }

            @Override
            public void onSuccess(MovieResult result) {
                if (result != null && result.getMovies() != null) {
                    emitter.onSuccess(result.getMovies());
                } else {
                    emitter.onError(new Throwable("No movie data found"));
                }
            }

            @Override
            public void onError(Throwable t) {
                emitter.onError(t);
            }
        };
    }
}
