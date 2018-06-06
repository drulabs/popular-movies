package org.drulabs.popularmovies.data;

import org.drulabs.popularmovies.data.models.Movie;

import java.util.List;

import io.reactivex.Single;

/**
 * This is the model layer of the application, all the data related operations passed through
 * this layer
 */
public interface DataHandler {
    Single<List<Movie>> fetchPopularMovies(int pageNumber);

    Single<List<Movie>> fetchTopRatedMovies(int pageNumber);

    Single<Movie> fetchMovieDetails(long movieId);
}
