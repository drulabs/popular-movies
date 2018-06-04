package org.drulabs.popularmovies.di;

import org.drulabs.popularmovies.ui.details.DetailActivity;
import org.drulabs.popularmovies.ui.details.DetailsContract;
import org.drulabs.popularmovies.ui.home.HomeActivity;
import org.drulabs.popularmovies.ui.home.HomeContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModule {

    private HomeActivity homeActivity;
    private DetailActivity detailActivity;

    public ViewModule(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public ViewModule(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Provides
    @ActivityScope
    HomeContract.View providesHomeView() {
        return homeActivity;
    }

    @Provides
    @ActivityScope
    DetailsContract.View providesDetailsView() {
        return detailActivity;
    }
}
