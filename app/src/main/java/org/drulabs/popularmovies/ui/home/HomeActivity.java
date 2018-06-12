package org.drulabs.popularmovies.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.application.AppClass;
import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.di.ActivityScope;
import org.drulabs.popularmovies.di.DaggerViewComponent;
import org.drulabs.popularmovies.di.ViewModule;
import org.drulabs.popularmovies.ui.custom.EndlessScrollListener;
import org.drulabs.popularmovies.ui.details.DetailActivity;
import org.drulabs.popularmovies.utils.Utility;

import java.util.List;

import javax.inject.Inject;

@ActivityScope
public class HomeActivity extends AppCompatActivity implements HomeContract.View, MovieAdapter
        .MovieInteractionListener, SwipeRefreshLayout.OnRefreshListener, AdapterView
        .OnItemSelectedListener {

    private static final int DEFAULT_SPAN_COUNT = 2;

    private static final int SELECTION_POPULAR_MOVIES = 0;
    private static final int SELECTION_TOP_RATED_MOVIES = 1;
    private int currentSelection = SELECTION_POPULAR_MOVIES;

    @Inject
    HomeContract.Presenter presenter;

    private MovieAdapter moviesAdapter;

    private SwipeRefreshLayout srHomeLayoutHolder;
    private RecyclerView rvMovies;
    private TextView tvNoInternet;

    private boolean isFirstLoadDone = false;

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

        if (Utility.isOnline(this)) {
            presenter.start();
        } else {
            tvNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void initializeUI() {

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        rvMovies = findViewById(R.id.rv_movies);

        int spanCount = calculateBestSpanCount();

        tvNoInternet = findViewById(R.id.tv_no_internet);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        rvMovies.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MovieAdapter(this, spanCount);
        rvMovies.setAdapter(moviesAdapter);

        srHomeLayoutHolder = findViewById(R.id.sr_home_layout_holder);
        srHomeLayoutHolder.setOnRefreshListener(this);

        Spinner spnSortOptions = findViewById(R.id.spn_sort_options);
        spnSortOptions.setOnItemSelectedListener(this);

        rvMovies.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isFirstLoadDone) {
                    switch (currentSelection) {
                        case SELECTION_POPULAR_MOVIES:
                            presenter.fetchPopularMovies(true);
                            break;
                        case SELECTION_TOP_RATED_MOVIES:
                            presenter.fetchTopRatedMovies(true);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void appendMovies(@NonNull List<Movie> movies) {
        moviesAdapter.append(movies);
        tvNoInternet.setVisibility(View.GONE);
    }

    @Override
    public void reload(@NonNull List<Movie> movies) {
        Log.d("reload", "reload: called");
        moviesAdapter.clearAndReload(movies);
        isFirstLoadDone = true;
        tvNoInternet.setVisibility(View.GONE);
    }

    @Override
    public void navigateToMovieDetails(Movie movie) {
        Intent detailsIntent = new Intent(this, DetailActivity.class);
        detailsIntent.putExtra(DetailActivity.KEY_MOVIE_ID, movie.getId());
        startActivity(detailsIntent);
    }

    @Override
    public void showLoading() {
        srHomeLayoutHolder.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        srHomeLayoutHolder.setRefreshing(false);
    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public HomeContract.Presenter getPresenter() {
        return presenter;
    }

    private int calculateBestSpanCount() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        int spanCount = Math.round(screenWidth / AppConstants.DEFAULT_POSTER_WIDTH);
        return (spanCount < DEFAULT_SPAN_COUNT) ? DEFAULT_SPAN_COUNT : spanCount;
    }

    @Override
    public void onClick(Movie movie) {
        presenter.onMovieTapped(movie);
    }

    @Override
    public void onRefresh() {
        // Layout is refreshed, load freshly
        isFirstLoadDone = false;
        switch (currentSelection) {
            case SELECTION_POPULAR_MOVIES:
                presenter.fetchPopularMovies(false);
                break;
            case SELECTION_TOP_RATED_MOVIES:
                presenter.fetchTopRatedMovies(false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (isFirstLoadDone) {
            if (position == 0) {
                currentSelection = SELECTION_POPULAR_MOVIES;
                presenter.fetchPopularMovies(false);
            } else {
                currentSelection = SELECTION_TOP_RATED_MOVIES;
                presenter.fetchTopRatedMovies(false);
            }
            rvMovies.smoothScrollToPosition(0);
            isFirstLoadDone = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openInternetSettings(View view) {
        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    }
}
