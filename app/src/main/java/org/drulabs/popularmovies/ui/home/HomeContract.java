package org.drulabs.popularmovies.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.ui.BasePresenter;
import org.drulabs.popularmovies.ui.BaseView;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void appendMovies(List<Movie> movies);

        void reload(List<Movie> movies, int category);

        void navigateToMovieDetails(Movie movie);
    }

    interface Presenter extends BasePresenter {
        void start();

        void onMovieTapped(Movie movie);

        /**
         * Fetches popular movies from remote source (TMDB)
         *
         * @param loadNextBatch if true, loads next batch, loads fresh batch otherwise
         */
        void fetchPopularMovies(boolean loadNextBatch);

        /**
         * Fetches popular movies from remote source (TMDB)
         *
         * @param loadNextBatch if true, loads next batch, loads fresh batch otherwise
         */
        void fetchTopRatedMovies(boolean loadNextBatch);

        /**
         * Fetches all favorite movies from local database (Room),
         *
         * @param appCompatActivity for observing lifecycle events
         */
        void fetchAllFavoriteMovies(@NonNull AppCompatActivity appCompatActivity);

        /**
         * Fetches popular movies from local database (Room),
         *
         * @param appCompatActivity for observing lifecycle events
         */
        void fetchCachedPopularMovies(@NonNull AppCompatActivity appCompatActivity);

        /**
         * Fetches top rated movies from local database (Room),
         *
         * @param appCompatActivity for observing lifecycle events
         */
        void fetchCachedTopRatedMovies(@NonNull AppCompatActivity appCompatActivity);
    }
}