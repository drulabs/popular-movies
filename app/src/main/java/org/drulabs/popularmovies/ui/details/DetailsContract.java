package org.drulabs.popularmovies.ui.details;

import android.support.v7.app.AppCompatActivity;

import org.drulabs.popularmovies.ui.BasePresenter;
import org.drulabs.popularmovies.ui.BaseView;

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
    }

    interface Presenter extends BasePresenter {
        void start(long movieId);

        void favoriteTapped();

        void observeCurrentMovie(AppCompatActivity context, long movieId, boolean
                executeAnimations);
    }

}
