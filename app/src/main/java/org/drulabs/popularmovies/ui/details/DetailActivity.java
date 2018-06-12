package org.drulabs.popularmovies.ui.details;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.application.AppClass;
import org.drulabs.popularmovies.di.ActivityScope;
import org.drulabs.popularmovies.di.DaggerViewComponent;
import org.drulabs.popularmovies.di.ViewModule;

import java.util.Locale;

import javax.inject.Inject;

@ActivityScope
public class DetailActivity extends AppCompatActivity implements DetailsContract.View {

    public static final String KEY_MOVIE_ID = "movie_id";

    private static final float ALPHA_GREYED_OUT = 0.3f;
    private static final float ALPHA_REGULAR = 1.0f;

    private View clDetailsHolder;
    private ProgressBar pbDetailsLoader;
    private ImageView imgPoster, imgBackdrop, imgBackground;
    private TextView tvReleaseYear, tvRunTime, tvRating, tvSynopsis;
    private CollapsingToolbarLayout toolbarLayout;
    private NestedScrollView nsvDetails;
    // private Toolbar toolbar;

    @Inject
    DetailsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_new);

        initializeUI();

        DaggerViewComponent.builder()
                .appComponent(((AppClass) getApplicationContext()).getAppComponent())
                .viewModule(new ViewModule(this))
                .build()
                .inject(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_MOVIE_ID)) {
            long movieId = extras.getLong(KEY_MOVIE_ID);
            presenter.start(movieId);
        } else {
            onError();
            finish();
        }
    }

    private void initializeUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        nsvDetails = findViewById(R.id.nsv_details_holder);

        clDetailsHolder = findViewById(R.id.cl_details_holder);
        pbDetailsLoader = findViewById(R.id.pb_details_loader);

        toolbarLayout = findViewById(R.id.toolbar_details_collapsing);

        imgPoster = findViewById(R.id.img_detail_poster);
        imgBackdrop = findViewById(R.id.img_detail_backdrop);
        imgBackground = findViewById(R.id.img_background_image);

        tvReleaseYear = findViewById(R.id.tv_detail_movie_year);
        tvRunTime = findViewById(R.id.tv_detail_movie_runtime);
        tvRating = findViewById(R.id.tv_detail_movie_rating);
        tvSynopsis = findViewById(R.id.tv_details_synopsis);
    }

    @Override
    public void loadPoster(String posterUrl) {
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_dead_smiley)
                .into(imgPoster);
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_dead_smiley)
                .into(imgBackground);
        animate(imgPoster);
    }

    @Override
    public void loadBackdrop(String backdropUrl) {
        Picasso.with(this)
                .load(backdropUrl)
                .placeholder(R.drawable.ic_backdrop)
                .error(R.drawable.ic_backdrop)
                .into(imgBackdrop);
    }

    @Override
    public void loadTitle(String title) {
        toolbarLayout.setTitle(title);
    }

    @Override
    public void loadReleaseDate(String releaseDate) {
        tvReleaseYear.setVisibility(View.VISIBLE);
        tvReleaseYear.setText(releaseDate);
    }

    @Override
    public void loadRuntime(String runtime) {
        tvRunTime.setVisibility(View.VISIBLE);
        tvRunTime.setText(String.format(Locale.getDefault(), getString(R.string.format_runtime),
                runtime));
    }

    @Override
    public void loadRating(String rating) {
        tvRating.setVisibility(View.VISIBLE);
        tvRating.setText(String.format(Locale.getDefault(), getString(R.string.format_rating),
                rating));
    }

    @Override
    public void loadSummary(String summary) {
        tvSynopsis.setText(summary);
    }

    @Override
    public void showLoading() {
        clDetailsHolder.setAlpha(ALPHA_GREYED_OUT);
        pbDetailsLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        clDetailsHolder.setAlpha(ALPHA_REGULAR);
        pbDetailsLoader.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public DetailsContract.Presenter getPresenter() {
        return presenter;
    }

    private void animate(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_pop_up_animation);
        viewToAnimate.startAnimation(animation);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, android.R.anim
                .slide_in_left);
        nsvDetails.startAnimation(slideUpAnimation);
    }
}
