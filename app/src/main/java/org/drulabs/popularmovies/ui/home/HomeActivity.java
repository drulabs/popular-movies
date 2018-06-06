package org.drulabs.popularmovies.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.application.AppClass;
import org.drulabs.popularmovies.data.models.Movie;
import org.drulabs.popularmovies.di.ActivityScope;
import org.drulabs.popularmovies.di.DaggerViewComponent;
import org.drulabs.popularmovies.di.ViewModule;
import org.drulabs.popularmovies.ui.details.DetailActivity;

import java.util.List;

import javax.inject.Inject;

@ActivityScope
public class HomeActivity extends AppCompatActivity implements HomeContract.View, MovieAdapter
        .MovieInteractionListener, SwipeRefreshLayout.OnRefreshListener, AdapterView
        .OnItemSelectedListener {

    private static final int RECYCLER_VIEW_SPAN_COUNT = 2;

    private static final float ALPHA_GREYED_OUT = 0.3f;
    private static final float ALPHA_REGULAR = 1.0f;

    private static final int SELECTION_POPULAR_MOVIES = 0;
    private static final int SELECTION_TOP_RATED_MOVIES = 1;
    private int currentSelection = SELECTION_POPULAR_MOVIES;

    @Inject
    HomeContract.Presenter presenter;

    private MovieAdapter moviesAdapter;

    private SwipeRefreshLayout srHomeLayoutHolder;
    private Button btnLoadMore;
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

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        rvMovies = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, RECYCLER_VIEW_SPAN_COUNT);
        rvMovies.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MovieAdapter(this);
        rvMovies.setAdapter(moviesAdapter);

        srHomeLayoutHolder = findViewById(R.id.sr_home_layout_holder);
        srHomeLayoutHolder.setOnRefreshListener(this);

        Spinner spnSortOptions = findViewById(R.id.spn_sort_options);
        spnSortOptions.setOnItemSelectedListener(this);

        btnLoadMore = findViewById(R.id.btn_load_more);
        btnLoadMore.setOnClickListener(v -> {
            if (!srHomeLayoutHolder.isRefreshing()) {
                btnLoadMore.setVisibility(View.GONE);
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
        });

        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    btnLoadMore.setVisibility(View.VISIBLE);
                } else {
                    btnLoadMore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        rvMovies.setSaveEnabled(true);
    }

    @Override
    public void appendMovies(@NonNull List<Movie> movies) {
        moviesAdapter.append(movies);
    }

    @Override
    public void reload(@NonNull List<Movie> movies) {
        moviesAdapter.clearAndReload(movies);
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
        rvMovies.setAlpha(ALPHA_GREYED_OUT);
    }

    @Override
    public void hideLoading() {
        srHomeLayoutHolder.setRefreshing(false);
        rvMovies.setAlpha(ALPHA_REGULAR);
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
        presenter.onMovieTapped(movie);
    }

    @Override
    public void onRefresh() {
        // Layout is refreshed, load freshly
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
        if (position == 0) {
            currentSelection = SELECTION_POPULAR_MOVIES;
            presenter.fetchPopularMovies(false);
        } else {
            currentSelection = SELECTION_TOP_RATED_MOVIES;
            presenter.fetchTopRatedMovies(false);
        }
        rvMovies.smoothScrollToPosition(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
