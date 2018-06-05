package org.drulabs.popularmovies.ui.home;

import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.ui.BasePresenter;
import org.drulabs.popularmovies.ui.BaseView;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void appendMovies(List<Movie> movies);

        void reload(List<Movie> movies);

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
    }
}