package org.drulabs.popularmovies.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.models.Movie;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM moviesdb WHERE favorite = :favorite")
    LiveData<List<Movie>> loadFavoriteMovies(boolean favorite);

    @Query("SELECT * FROM moviesdb WHERE category = " + AppConstants.SELECTION_POPULAR_MOVIES + " order by popularity DESC")
    LiveData<List<Movie>> loadPopularMovies();

    @Query("SELECT * FROM moviesdb WHERE category = " + AppConstants.SELECTION_TOP_RATED_MOVIES + " order by averageVote DESC")
    LiveData<List<Movie>> loadTopRatedMovies();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMovies(List<Movie> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Query("SELECT * FROM moviesdb WHERE id = :movieId")
    LiveData<Movie> loadMovie(long movieId);

}
