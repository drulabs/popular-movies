package org.drulabs.popularmovies.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
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

    private static final int DEFAULT_SPAN_COUNT = AppConstants.DEFAULT_SPAN_COUNT;
    private static final int MAX_SPAN_COUNT = AppConstants.MAX_SPAN_COUNT;
    private static final String KEY_CURRENT_SELECTION = "currentSpinnerSelection";
    private static final String KEY_FIRST_VISIBLE_ITEM = "firstVisibleItem";

    private int currentSelection = AppConstants.SELECTION_POPULAR_MOVIES;

    @Inject
    HomeContract.Presenter presenter;

    private MovieAdapter moviesAdapter;

    private SwipeRefreshLayout srHomeLayoutHolder;
    private RecyclerView rvMovies;
    private TextView tvNoDataMessage;

    private int firstVisibleItem = 0;

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

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_SELECTION)) {
            currentSelection = savedInstanceState.getInt(KEY_CURRENT_SELECTION);
            firstVisibleItem = savedInstanceState.getInt(KEY_FIRST_VISIBLE_ITEM);
            loadCachedData(currentSelection);
        } else {
            if (Utility.isOnline(this)) {
                presenter.start();
            } else {
                presenter.fetchCachedPopularMovies(this);
            }
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

        tvNoDataMessage = findViewById(R.id.tv_no_data_message);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        rvMovies.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MovieAdapter(this, spanCount);
        rvMovies.setAdapter(moviesAdapter);

        srHomeLayoutHolder = findViewById(R.id.sr_home_layout_holder);
        srHomeLayoutHolder.setOnRefreshListener(this);

        Spinner spnSortOptions = findViewById(R.id.spn_sort_options);
        spnSortOptions.setSelected(false);
        spnSortOptions.setSelection(currentSelection, false);
        spnSortOptions.setOnItemSelectedListener(this);

        rvMovies.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                switch (currentSelection) {
                    case AppConstants.SELECTION_POPULAR_MOVIES:
                        presenter.fetchPopularMovies(true);
                        break;
                    case AppConstants.SELECTION_TOP_RATED_MOVIES:
                        presenter.fetchTopRatedMovies(true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void appendMovies(@NonNull List<Movie> movies) {
        moviesAdapter.append(movies);
        tvNoDataMessage.setVisibility(View.GONE);
    }

    @Override
    public void reload(@NonNull List<Movie> movies, int category) {
        Log.d("reload", "reload: called");

        if (category != currentSelection) {
            return;
        }

        @StringRes int msgResId;
        if (Utility.isOnline(this)) {
            msgResId = R.string.txt_no_data;
        } else {
            msgResId = R.string.text_no_internet;
        }

        if (movies.size() > 0) {
            rvMovies.setVisibility(View.VISIBLE);
            moviesAdapter.clearAndReload(movies);
            tvNoDataMessage.setVisibility(View.GONE);
            if (firstVisibleItem < movies.size()) {
                rvMovies.scrollToPosition(firstVisibleItem);
                firstVisibleItem = 0;
            }
        } else {
            tvNoDataMessage.setText(msgResId);
            tvNoDataMessage.setVisibility(View.VISIBLE);
            rvMovies.setVisibility(View.GONE);
        }
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
        return (spanCount < DEFAULT_SPAN_COUNT) ? DEFAULT_SPAN_COUNT : (spanCount >
                MAX_SPAN_COUNT ? MAX_SPAN_COUNT : spanCount);
    }

    @Override
    public void onClick(Movie movie) {
        presenter.onMovieTapped(movie);
    }

    @Override
    public void onRefresh() {
        // Layout is refreshed, load freshly
        boolean isOnline = Utility.isOnline(this);
        switch (currentSelection) {
            case AppConstants.SELECTION_POPULAR_MOVIES:
                if (isOnline) {
                    presenter.fetchPopularMovies(false);
                } else {
                    presenter.fetchCachedPopularMovies(this);
                }
                break;
            case AppConstants.SELECTION_TOP_RATED_MOVIES:
                if (isOnline) {
                    presenter.fetchTopRatedMovies(false);
                } else {
                    presenter.fetchCachedTopRatedMovies(this);
                }
                break;
            case AppConstants.SELECTION_FAVORITE_MOVIES:
                presenter.fetchAllFavoriteMovies(this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_SELECTION, currentSelection);
        int firstVisibleItem = ((GridLayoutManager) rvMovies.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
        outState.putInt(KEY_FIRST_VISIBLE_ITEM, firstVisibleItem);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(KEY_CURRENT_SELECTION)) {
//                currentSelection = savedInstanceState.getInt(KEY_CURRENT_SELECTION);
//                onRefresh();
//            }
//            if (savedInstanceState.containsKey("pos")) {
//                int position = savedInstanceState.getInt("pos");
//                rvMovies.smoothScrollToPosition(position);
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSelection = position;
        boolean isOnline = Utility.isOnline(this);
        switch (position) {
            case AppConstants.SELECTION_POPULAR_MOVIES:
                if (isOnline) {
                    presenter.fetchPopularMovies(false);
                } else {
                    presenter.fetchCachedPopularMovies(this);
                }
                break;
            case AppConstants.SELECTION_TOP_RATED_MOVIES:
                if (isOnline) {
                    presenter.fetchTopRatedMovies(false);
                } else {
                    presenter.fetchCachedTopRatedMovies(this);
                }
                break;
            default:
                presenter.fetchAllFavoriteMovies(this);
                break;
        }
        rvMovies.smoothScrollToPosition(0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openInternetSettings(View view) {
        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    }

    private void loadCachedData(int currentSelection) {
        switch (currentSelection) {
            case AppConstants.SELECTION_POPULAR_MOVIES:
                presenter.fetchCachedPopularMovies(this);
                break;
            case AppConstants.SELECTION_TOP_RATED_MOVIES:
                presenter.fetchCachedTopRatedMovies(this);
                break;
            case AppConstants.SELECTION_FAVORITE_MOVIES:
                presenter.fetchAllFavoriteMovies(this);
                break;
            default:
                break;
        }
    }

    public void onAdvertisementTapped(View view) {
        String url = "https://discover-omnyway.firebaseapp.com/zb/newZb.html?oid=1aa714fa-4f7a-4b05-8f99-73a716183b12&remote=dev";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
