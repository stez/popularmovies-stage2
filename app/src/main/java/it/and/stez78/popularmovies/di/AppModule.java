package it.and.stez78.popularmovies.di;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.and.stez78.popularmovies.BuildConfig;
import it.and.stez78.popularmovies.network.ApiKeyInterceptor;
import it.and.stez78.popularmovies.network.TheMovieDbApiService;
import it.and.stez78.popularmovies.utils.TheMovieDbUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stefano on 18/02/18.
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new ApiKeyInterceptor())
                .build();
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application ctx) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return new Picasso.Builder(ctx)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(TheMovieDbUtils.TMDB_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    TheMovieDbApiService provideMovieDbApiService(Retrofit retrofit) {
        return retrofit.create(TheMovieDbApiService.class);
    }

}
