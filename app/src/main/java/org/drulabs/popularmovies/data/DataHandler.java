package org.drulabs.popularmovies.data;

import org.drulabs.popularmovies.data.models.Movie;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * This is the model layer of the application, all the data related operations passed through
 * this layer
 */
public interface DataHandler {
    Observable<Movie> fetchPopularMovies(int pageNumber);

    Observable<Movie> fetchTopRatedMovies(int pageNumber);

    Single<Movie> fetchMovieDetails(int movieId);
}
