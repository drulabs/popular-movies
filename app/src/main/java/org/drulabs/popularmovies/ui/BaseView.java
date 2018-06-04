package org.drulabs.popularmovies.ui;

public interface BaseView<T> {

    void showLoading();

    void hideLoading();

    void onError();

    T getPresenter();
}
