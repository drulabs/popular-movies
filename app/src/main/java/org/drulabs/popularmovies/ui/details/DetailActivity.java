package org.drulabs.popularmovies.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.models.Cast;
import org.drulabs.popularmovies.data.models.Review;
import org.drulabs.popularmovies.data.models.Video;
import org.drulabs.popularmovies.di.ActivityScope;
import org.drulabs.popularmovies.di.DaggerViewComponent;
import org.drulabs.popularmovies.di.ViewModule;
import org.drulabs.popularmovies.utils.Utility;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

@ActivityScope
public class DetailActivity extends AppCompatActivity implements DetailsContract.View,
        VideoAdapter.Listener, CastAdapter.Listener, ReviewAdapter.Listener {

    public static final String KEY_MOVIE_ID = "movie_id";

    private static final float ALPHA_GREYED_OUT = 0.3f;
    private static final float ALPHA_REGULAR = 1.0f;

    private View clDetailsHolder;
    private ProgressBar pbDetailsLoader;
    private ImageView imgBackdrop, imgBackground, imgFavorite, imgPoster;
    private TextView tvReleaseYear, tvRunTime, tvRating, tvSynopsis;
    private CollapsingToolbarLayout toolbarLayout;
    private NestedScrollView nsvDetails;
    private RecyclerView rvVideos, rvCasts, rvReviews;
    private TextView tvVideosAlternate, tvCastsAlternate, tvReviewsAlternate;
    // private Toolbar toolbar;

    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private CastAdapter castAdapter;

    private String firstTrailerUrl;

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

            boolean isOnline = Utility.isOnline(this);
            if (isOnline) {
                presenter.start(movieId);
            }
            presenter.observeCurrentMovie(this, movieId, !isOnline);
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

        imgBackdrop = findViewById(R.id.img_detail_backdrop);
        imgBackground = findViewById(R.id.img_background_image);
        imgFavorite = findViewById(R.id.img_detail_favorite);
        imgPoster = findViewById(R.id.imgPoster);

        tvReleaseYear = findViewById(R.id.tv_detail_movie_year);
        tvRunTime = findViewById(R.id.tv_detail_movie_runtime);
        tvRating = findViewById(R.id.tv_detail_movie_rating);
        tvSynopsis = findViewById(R.id.tv_details_synopsis);

        rvCasts = findViewById(R.id.rv_details_cast);
        tvCastsAlternate = findViewById(R.id.tv_details_cast_alternate);
        castAdapter = new CastAdapter(this);
        rvCasts.setAdapter(castAdapter);

        rvVideos = findViewById(R.id.rv_details_trailers);
        tvVideosAlternate = findViewById(R.id.tv_details_trailers_alternate);
        videoAdapter = new VideoAdapter(this);
        rvVideos.setAdapter(videoAdapter);

        rvReviews = findViewById(R.id.rv_details_reviews);
        tvReviewsAlternate = findViewById(R.id.tv_details_reviews_alternate);
        reviewAdapter = new ReviewAdapter(this);
        rvReviews.setAdapter(reviewAdapter);

        imgFavorite.setOnClickListener(v -> presenter.favoriteTapped());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareTrailer(firstTrailerUrl);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadPoster(String posterUrl) {
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(imgBackground);
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(imgPoster);

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
    public void markFavorite() {
        imgFavorite.setImageResource(R.drawable.ic_star_filled);
    }

    @Override
    public void unmarkFavorite() {
        imgFavorite.setImageResource(R.drawable.ic_star_filled_gray);
    }

    @Override
    public void executeAnimations() {
        animate(imgBackdrop);
    }

    @Override
    public void loadVideos(@NonNull List<Video> videoList) {
        if (videoList.isEmpty()) {
            tvVideosAlternate.setVisibility(View.VISIBLE);
            rvVideos.setVisibility(View.GONE);
        } else {
            tvVideosAlternate.setVisibility(View.GONE);
            rvVideos.setVisibility(View.VISIBLE);
            videoAdapter.reload(videoList);
            Video firstTrailer = videoList.get(0);
            firstTrailerUrl = String.format(Locale.getDefault(), AppConstants
                    .MOVIE_VIDEO_BASE, firstTrailer.getKey());
        }
    }

    @Override
    public void hideVideoSection() {
        tvVideosAlternate.setVisibility(View.VISIBLE);
        rvVideos.setVisibility(View.GONE);
    }

    @Override
    public void loadCast(@NonNull List<Cast> castList) {
        if (castList.isEmpty()) {
            tvCastsAlternate.setVisibility(View.VISIBLE);
            rvCasts.setVisibility(View.GONE);
        } else {
            tvCastsAlternate.setVisibility(View.GONE);
            rvCasts.setVisibility(View.VISIBLE);
            castAdapter.reload(castList);
        }
    }

    @Override
    public void hideCastSection() {
        tvCastsAlternate.setVisibility(View.VISIBLE);
        rvCasts.setVisibility(View.GONE);
    }

    @Override
    public void loadReviews(@NonNull List<Review> reviewList) {
        if (reviewList.isEmpty()) {
            tvReviewsAlternate.setVisibility(View.VISIBLE);
            rvReviews.setVisibility(View.GONE);
        } else {
            tvReviewsAlternate.setVisibility(View.GONE);
            rvReviews.setVisibility(View.VISIBLE);
            reviewAdapter.reload(reviewList);
        }
    }

    @Override
    public void hideReviewsSection() {
        tvReviewsAlternate.setVisibility(View.VISIBLE);
        rvReviews.setVisibility(View.GONE);
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

    @Override
    public void onVideoTapped(Video video) {
        String youtubeUrl = String.format(Locale.getDefault(), AppConstants
                .MOVIE_VIDEO_BASE, video.getKey());
        Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setData(Uri.parse(youtubeUrl));
        startActivity(videoIntent);
    }

    @Override
    public void onShareTapped(Video video) {
        String youtubeUrl = String.format(Locale.getDefault(), AppConstants
                .MOVIE_VIDEO_BASE, video.getKey());
        shareTrailer(youtubeUrl);
    }

    private void shareTrailer(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared_via_pop_movies));
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            Intent chooserIntent = Intent.createChooser(shareIntent, getString(R.string.text_share_now));
            chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(chooserIntent);
        } else {
            Toast.makeText(this, getString(R.string.unable_to_share), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCastTapped(String urlToOpen) {
        Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setData(Uri.parse(urlToOpen));
        startActivity(videoIntent);
    }

    @Override
    public void onReviewTapped(String urlToOpen) {
        Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setData(Uri.parse(urlToOpen));
        startActivity(videoIntent);
    }
}
