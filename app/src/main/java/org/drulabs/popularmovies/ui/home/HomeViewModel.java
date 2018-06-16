package org.drulabs.popularmovies.ui.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import org.drulabs.popularmovies.data.local.MovieDatabase;
import org.drulabs.popularmovies.data.models.Movie;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private static final String TAG = "HomeVM";

    private LiveData<List<Movie>> favoriteMovies;
    private LiveData<List<Movie>> popularMovies;
    private LiveData<List<Movie>> topRatedMovies;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "HomeViewModel: database queried");
        favoriteMovies = movieDatabase.moviesDao().loadFavoriteMovies(true);
        popularMovies = movieDatabase.moviesDao().loadPopularMovies();
        topRatedMovies = movieDatabase.moviesDao().loadTopRatedMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return topRatedMovies;
    }
}
