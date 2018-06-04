package org.drulabs.popularmovies.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.application.AppClass;
import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.di.DaggerViewComponent;
import org.drulabs.popularmovies.di.ViewModule;

import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, MovieAdapter
        .MovieClickListener {

    private static final int RECYCLER_VIEW_SPAN_COUNT = 2;

    @Inject
    HomeContract.Presenter presenter;

    private MovieAdapter moviesAdapter;
    private RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeUI();

        DaggerViewComponent.builder()
                .appComponent(((AppClass) getApplicationContext()).getAppComponent())
                .viewModule(new ViewModule(this))
                .build()
                .inject(this);

        presenter.start();
    }

    private void initializeUI() {
        rvMovies = findViewById(R.id.rv_movies);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,
                RECYCLER_VIEW_SPAN_COUNT);
        rvMovies.setLayoutManager(layoutManager);
        moviesAdapter = new MovieAdapter(this);
        rvMovies.setAdapter(moviesAdapter);
    }

    @Override
    public void appendMovies(@NonNull List<Movie> movies) {
        moviesAdapter.append(movies);
    }

    @Override
    public void appendMovie(@NonNull Movie movie) {
        moviesAdapter.append(movie);
    }

    @Override
    public void reload(@NonNull List<Movie> movies) {
        moviesAdapter.clearAndReload(movies);
    }

    @Override
    public void navigateToMovieDetails(Movie movie) {

    }

    @Override
    public void showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        Toast.makeText(this, "Loading done.....", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public HomeContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void onClick(Movie movie) {

    }
}
