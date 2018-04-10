package it.and.stez78.popularmovies.network;

import java.io.IOException;

import it.and.stez78.popularmovies.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Stefano Zanotti on 06/12/2017.
 */
public class ApiKeyInterceptor implements Interceptor {

    private String apiKey = BuildConfig.API_KEY;

    @Override
    public Response intercept(Chain chain) throws IOException {

        HttpUrl url = chain.request().url().newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build();
        Request.Builder requestBuilder = chain.request().newBuilder().url(url);
        return chain.proceed(requestBuilder.build());
    }
}
