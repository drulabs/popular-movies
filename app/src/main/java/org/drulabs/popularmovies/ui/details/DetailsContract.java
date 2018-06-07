package org.drulabs.popularmovies.ui.details;

import org.drulabs.popularmovies.ui.BasePresenter;
import org.drulabs.popularmovies.ui.BaseView;

public interface DetailsContract {

    interface View extends BaseView<Presenter> {
        void loadPoster(String posterUrl);
        void loadBackdrop(String backdropUrl);
        void loadTitle(String title);
        void loadYear(String year);
        void loadRuntime(String runtime);
        void loadRating(String rating);
        void loadSummary(String summary);
    }

    interface Presenter extends BasePresenter {
        void start(long movieId);
        void favoriteTapped(boolean isFavorite);
    }

}
