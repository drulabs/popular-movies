package org.drulabs.popularmovies.data;

import org.drulabs.popularmovies.data.models.Cast;
import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.data.models.Review;
import org.drulabs.popularmovies.data.models.Video;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * This is the model layer of the application, all the data related operations passed through
 * this layer
 */
public interface DataHandler {
    Observable<List<Movie>> fetchPopularMovies(int pageNumber);

    Observable<List<Movie>> fetchTopRatedMovies(int pageNumber);

    Single<Movie> fetchMovieDetails(long movieId);

    Observable<List<Review>> fetchMovieReviews(long movieId);

    Observable<List<Video>> fetchMovieVideos(long movieId);

    Observable<List<Cast>> fetchMovieCast(long movieId);

    Completable addFavoriteMovie(Movie movie);

    Completable deleteFavoritedMovie(Movie currentMovie);
}
