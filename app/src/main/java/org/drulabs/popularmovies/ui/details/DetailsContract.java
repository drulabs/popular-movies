package org.drulabs.popularmovies.ui.details;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.drulabs.popularmovies.data.models.Cast;
import org.drulabs.popularmovies.data.models.Review;
import org.drulabs.popularmovies.data.models.Video;
import org.drulabs.popularmovies.ui.BasePresenter;
import org.drulabs.popularmovies.ui.BaseView;

import java.util.List;

public interface DetailsContract {

    interface View extends BaseView<Presenter> {
        void loadPoster(String posterUrl);

        void loadBackdrop(String backdropUrl);

        void loadTitle(String title);

        void loadReleaseDate(String releaseDate);

        void loadRuntime(String runtime);

        void loadRating(String rating);

        void loadSummary(String summary);

        void markFavorite();

        void unmarkFavorite();

        void executeAnimations();

        void loadVideos(@NonNull List<Video> videoList);

        void hideVideoSection();

        void loadCast(@NonNull List<Cast> castList);

        void hideCastSection();

        void loadReviews(@NonNull List<Review> reviewList);

        void hideReviewsSection();
    }

    interface Presenter extends BasePresenter {
        void start(long movieId);

        void favoriteTapped();

        void observeCurrentMovie(AppCompatActivity context, long movieId, boolean
                executeAnimations);
    }

}
