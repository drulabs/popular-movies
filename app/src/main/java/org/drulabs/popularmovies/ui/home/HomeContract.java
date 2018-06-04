package org.drulabs.popularmovies.ui.home;

import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.ui.BasePresenter;
import org.drulabs.popularmovies.ui.BaseView;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void appendMovies(List<Movie> movies);

        void appendMovie(Movie movie);

        void reload(List<Movie> movies);

        void navigateToMovieDetails(Movie movie);
    }

    interface Presenter extends BasePresenter {
        void start();

        void onMovieTapped(Movie movie);

        void fetchPopularMovies();

        void fetchTopRatedMovies();
    }
}