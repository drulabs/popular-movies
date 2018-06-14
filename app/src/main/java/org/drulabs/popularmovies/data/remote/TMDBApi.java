package org.drulabs.popularmovies.data.remote;

import org.drulabs.popularmovies.data.models.CreditResult;
import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.data.models.MovieResult;
import org.drulabs.popularmovies.data.models.ReviewResult;
import org.drulabs.popularmovies.data.models.VideoResult;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for calling APIs
 */
public interface TMDBApi {

    String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    String TMDB_MOCK_URL = "mock url for testing";

    @GET("movie/popular")
    Observable<MovieResult> getPopularMovies(@Query("page") int pageNumber);

    @GET("movie/top_rated")
    Observable<MovieResult> getTopRatedMovies(@Query("page") int pageNumber);

    @GET("movie/{movieId}")
    Single<Movie> getMovieDetails(@Path("movieId") long movieId);

    @GET("movie/{movieId}/videos")
    Observable<VideoResult> getMovieVideos(@Path("movieId") long movieId);

    @GET("movie/{movieId}/reviews")
    Observable<ReviewResult> getMovieReviews(@Path("movieId") long movieId);

    @GET("movie/{movieId}/credits")
    Observable<CreditResult> getMovieCredits(@Path("movieId") long movieId);
}
