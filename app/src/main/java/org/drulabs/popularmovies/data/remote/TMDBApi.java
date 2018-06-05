package org.drulabs.popularmovies.data.remote;

import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.data.models.MovieResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for calling APIs
 */
public interface TMDBApi {

    String TMDB_BASE_URL = "https://api.themoviedb.org";
    String TMDB_MOCK_URL = "mock url for testing";

    @GET("/3/movie/popular")
    Single<MovieResult> getPopularMovies(@Query("page") int pageNumber);

    @GET("/3/movie/top_rated")
    Single<MovieResult> getTopRatedMovies(@Query("page") int pageNumber);

    @GET("/3/movie/{movieId}")
    Single<Movie> getMovieDetails(@Path("movieId") long movieId);
}
