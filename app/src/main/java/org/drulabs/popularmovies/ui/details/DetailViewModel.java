package org.drulabs.popularmovies.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.drulabs.popularmovies.data.local.MovieDatabase;
import org.drulabs.popularmovies.data.models.Movie;

public class DetailViewModel extends ViewModel {

    private LiveData<Movie> movieLiveData;

    public DetailViewModel(@NonNull MovieDatabase movieDatabase, long movieId) {
        movieLiveData = movieDatabase.moviesDao().loadMovie(movieId);
    }

    public LiveData<Movie> getMovieLiveData() {
        return movieLiveData;
    }
}
