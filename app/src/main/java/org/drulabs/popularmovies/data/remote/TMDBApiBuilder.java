package org.drulabs.popularmovies.data.remote;

import org.drulabs.popularmovies.BuildConfig;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBApiBuilder {

    private final static String PARAM_API_KEY = "api_key";

    private final Retrofit.Builder tmdbApiBuilder;

    public TMDBApiBuilder() {
        tmdbApiBuilder = new Retrofit.Builder().baseUrl(TMDBApi.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public TMDBApi build() {
        OkHttpClient okHttpClient = buildOkHttpClient();
        if (okHttpClient != null) {
            Retrofit retrofit = tmdbApiBuilder.client(okHttpClient).build();
            return retrofit.create(TMDBApi.class);
        }
        return null;
    }

    private OkHttpClient buildOkHttpClient() {
        try {

            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
            okHttpBuilder.addInterceptor(chain -> {

                Request originalReq = chain.request();
                HttpUrl originalHttpUrl = originalReq.url();

                // Adding API KEY query parameter
                HttpUrl newUrl = originalHttpUrl.newBuilder().addQueryParameter(PARAM_API_KEY,
                        BuildConfig.TMDB_API_KEY).build();

                Request.Builder reqBuilder = originalReq.newBuilder().url(newUrl);
                Request newReq = reqBuilder.build();

                return chain.proceed(newReq);
            });
            return okHttpBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
