package org.drulabs.popularmovies.data;

import android.content.Context;

import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.local.MovieDatabase;
import org.drulabs.popularmovies.data.local.MoviesDao;
import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.data.models.MovieResult;
import org.drulabs.popularmovies.data.remote.TMDBApi;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class AppDataHandler implements DataHandler {

    private final TMDBApi tmdbApi;
    private MoviesDao moviesDao;

    @Inject
    AppDataHandler(Context context, TMDBApi tmdbApi) {
        this.tmdbApi = tmdbApi;
        moviesDao = MovieDatabase.getInstance(context).moviesDao();
    }

    @Override
    public Observable<List<Movie>> fetchPopularMovies(int pageNumber) {
        return tmdbApi.getPopularMovies(pageNumber)
                .flatMap((Function<MovieResult, ObservableSource<List<Movie>>>) movieResult -> {
                    List<Movie> movies = movieResult.getMovies();
                    for (Movie movie : movies) {
                        movie.setCategory(AppConstants.SELECTION_POPULAR_MOVIES);
                    }
                    moviesDao.addMovies(movies);
                    return Observable.just(movies);
                });
    }

    @Override
    public Observable<List<Movie>> fetchTopRatedMovies(int pageNumber) {
        return tmdbApi.getTopRatedMovies(pageNumber)
                .flatMap((Function<MovieResult, ObservableSource<List<Movie>>>) movieResult -> {
                    List<Movie> movies = movieResult.getMovies();
                    for (Movie movie : movies) {
                        movie.setCategory(AppConstants.SELECTION_TOP_RATED_MOVIES);
                    }
                    moviesDao.addMovies(movies);
                    return Observable.just(movies);
                });
    }

    @Override
    public Single<Movie> fetchMovieDetails(long movieId) {
        return tmdbApi.getMovieDetails(movieId);
    }

    @Override
    public Completable addFavoriteMovie(Movie movie) {
        return Completable.fromAction(() -> {
            movie.setFavorite(true);
            moviesDao.updateMovie(movie);
        });
    }

    @Override
    public Completable deleteFavoritedMovie(Movie currentMovie) {
        return Completable.fromAction(() -> {
            currentMovie.setFavorite(false);
            moviesDao.updateMovie(currentMovie);
        });
    }
}
