package org.drulabs.popularmovies.di;

import org.drulabs.popularmovies.ui.details.DetailActivity;
import org.drulabs.popularmovies.ui.home.HomeActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {ViewModule.class, PresenterModule.class})
public interface ViewComponent {

    void inject(HomeActivity homeActivity);

    void inject(DetailActivity detailActivity);

}
