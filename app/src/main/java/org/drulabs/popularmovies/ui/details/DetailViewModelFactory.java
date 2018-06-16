package org.drulabs.popularmovies.ui.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.drulabs.popularmovies.data.local.MovieDatabase;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final MovieDatabase movieDatabase;
    private final long movieId;

    public DetailViewModelFactory(MovieDatabase movieDatabase, long movieId) {
        this.movieDatabase = movieDatabase;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(movieDatabase, movieId);
    }
}
