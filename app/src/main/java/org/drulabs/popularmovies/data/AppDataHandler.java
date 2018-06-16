package org.drulabs.popularmovies.data;

import android.content.Context;

import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.local.MovieDatabase;
import org.drulabs.popularmovies.data.local.MoviesDao;
import org.drulabs.popularmovies.data.models.Cast;
import org.drulabs.popularmovies.data.models.CreditResult;
import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.data.models.MovieResult;
import org.drulabs.popularmovies.data.models.Review;
import org.drulabs.popularmovies.data.models.ReviewResult;
import org.drulabs.popularmovies.data.models.Video;
import org.drulabs.popularmovies.data.models.VideoResult;
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
    public Observable<List<Review>> fetchMovieReviews(long movieId) {
        return tmdbApi.getMovieReviews(movieId)
                .flatMap((Function<ReviewResult, ObservableSource<List<Review>>>) reviewResult ->
                        Observable.just(reviewResult.getReviews()));
    }

    @Override
    public Observable<List<Video>> fetchMovieVideos(long movieId) {
        return tmdbApi.getMovieVideos(movieId)
                .flatMap((Function<VideoResult, ObservableSource<List<Video>>>) videoResult ->
                        Observable.just(videoResult.getVideos()));
    }

    @Override
    public Observable<List<Cast>> fetchMovieCast(long movieId) {
        return tmdbApi.getMovieCredits(movieId)
                .flatMap((Function<CreditResult, ObservableSource<List<Cast>>>) creditResult ->
                        Observable.just(creditResult.getCast()));
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
