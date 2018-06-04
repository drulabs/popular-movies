package org.drulabs.popularmovies.ui.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.application.AppClass;
import org.drulabs.popularmovies.di.DaggerViewComponent;
import org.drulabs.popularmovies.di.ViewModule;

import javax.inject.Inject;

public class DetailActivity extends AppCompatActivity implements DetailsContract.View {

    public static final String KEY_MOVIE_ID = "movie_id";

    @Inject
    DetailsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DaggerViewComponent.builder()
                .appComponent(((AppClass) getApplicationContext()).getAppComponent())
                .viewModule(new ViewModule(this))
                .build()
                .inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_MOVIE_ID)) {
            int movieId = extras.getInt(KEY_MOVIE_ID);
            presenter.start(movieId);
        } else {
            onError();
            finish();
        }
    }

    @Override
    public void loadPoster(String posterUrl) {

    }

    @Override
    public void loadTitle(String title) {

    }

    @Override
    public void loadYear(String year) {

    }

    @Override
    public void loadRuntime(String runtime) {

    }

    @Override
    public void loadRating(String rating) {

    }

    @Override
    public void loadSummary(String summary) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public DetailsContract.Presenter getPresenter() {
        return null;
    }
}
