package it.and.stez78.popularmovies.network;

import io.reactivex.Observable;
import it.and.stez78.popularmovies.model.MovieReview;
import it.and.stez78.popularmovies.model.MovieSearchResponse;
import it.and.stez78.popularmovies.model.MovieSearchResult;
import it.and.stez78.popularmovies.model.MovieVideo;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by stefano on 19/02/18.
 */

public interface TheMovieDbApiService {

    @GET("movie/popular")
    Observable<MovieSearchResponse<MovieSearchResult>> getPopular();

    @GET("movie/top_rated")
    Observable<MovieSearchResponse<MovieSearchResult>> getTopRated();

    @GET("movie/{movieId}/reviews")
    Observable<MovieSearchResponse<MovieReview>> getReviews(@Path("movieId") Long movieId);

    @GET("movie/{movieId}/videos")
    Observable<MovieSearchResponse<MovieVideo>> getVideos(@Path("movieId") Long movieId);
}
