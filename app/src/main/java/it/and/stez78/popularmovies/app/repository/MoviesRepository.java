package it.and.stez78.popularmovies.app.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import it.and.stez78.popularmovies.db.MovieContract;
import it.and.stez78.popularmovies.model.MovieReview;
import it.and.stez78.popularmovies.model.MovieSearchResult;
import it.and.stez78.popularmovies.model.MovieVideo;
import it.and.stez78.popularmovies.network.TheMovieDbApiService;

/**
 * Created by stefano on 19/03/18.
 * Repository that holds all the methods to access and manipulates Movies, Videos and Reviews
 * Mostly produces LiveData for the getters part
 */

public class MoviesRepository {

    private static final String MOVIE_TYPE_TRAILER = "Trailer";
    private static final String MOVIE_SITE_YOUTUBE = "YouTube";

    private Context context;
    private TheMovieDbApiService theMovieDbApiService;

    @Inject
    public MoviesRepository(Application context, TheMovieDbApiService theMovieDbApiService) {
        this.context = context;
        this.theMovieDbApiService = theMovieDbApiService;
    }

    public LiveData<List<MovieSearchResult>> getPopular() {
        MutableLiveData<List<MovieSearchResult>> moviesLivaData = new MutableLiveData<>();
        theMovieDbApiService.getPopular().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    moviesLivaData.postValue(result.getResults());
                }, error -> {
                    moviesLivaData.postValue(null);
                });
        return moviesLivaData;
    }

    public LiveData<List<MovieSearchResult>> getTopRated() {
        MutableLiveData<List<MovieSearchResult>> moviesLivaData = new MutableLiveData<>();
        theMovieDbApiService.getTopRated().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    moviesLivaData.postValue(result.getResults());
                }, error -> {
                    moviesLivaData.postValue(null);
                });
        return moviesLivaData;
    }

    public LiveData<List<MovieSearchResult>> getFavorite() {
        MutableLiveData<List<MovieSearchResult>> moviesLivaData = new MutableLiveData<>();
        getFavoriteMoviesObservable().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    moviesLivaData.postValue(result);
                }, error -> {
                    moviesLivaData.postValue(null);
                });
        return moviesLivaData;
    }

    public LiveData<List<MovieVideo>> getYoutubeTrailers(Long movieId) {
        MutableLiveData<List<MovieVideo>> youtubeTrailersLiveData = new MutableLiveData<>();
        theMovieDbApiService.getVideos(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    List<MovieVideo> list = new ArrayList<>();
                    for (MovieVideo mv : result.getResults()) {
                        if (isYoutubeTrailer(mv)) {
                            list.add(mv);
                        }
                    }
                    youtubeTrailersLiveData.postValue(list);
                }, error -> {
                    youtubeTrailersLiveData.postValue(null);
                });
        return youtubeTrailersLiveData;
    }

    public LiveData<List<MovieReview>> getReviews(Long movieId) {
        MutableLiveData<List<MovieReview>> reviewsLiveData = new MutableLiveData<>();
        theMovieDbApiService.getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    reviewsLiveData.postValue(result.getResults());
                }, error -> {
                    reviewsLiveData.postValue(null);
                });
        return reviewsLiveData;
    }

    public boolean isFavorite(Long movieId) {
        MutableLiveData<Boolean> isFavoriteLiveData = new MutableLiveData<>();
        Uri moviesUri = MovieContract.MovieEntry.CONTENT_URI;
        Uri movieWithIDUri = moviesUri.buildUpon().appendPath(movieId + "").build();
        Cursor cursor = context.getContentResolver().query(movieWithIDUri,
                null,
                null,
                null,
                null);
        return (cursor != null && cursor.getCount() == 1);
    }

    public boolean setFavorite(MovieSearchResult movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage() + "");
        Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        return uri != null;
    }

    public boolean removeFavorite(Long movieId) {
        Uri moviesUri = MovieContract.MovieEntry.CONTENT_URI;
        Uri movieWithIDUri = moviesUri.buildUpon().appendPath(movieId + "").build();
        int moviesDeleted = context.getContentResolver().delete(movieWithIDUri,
                null,
                null);
        return moviesDeleted == 1;
    }

    private Observable<List<MovieSearchResult>> getFavoriteMoviesObservable() {
        Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        return Observable.just(getMovieListFromCursor(cursor));
    }

    private List<MovieSearchResult> getMovieListFromCursor(Cursor cursor) {
        List<MovieSearchResult> movies = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            int idPos = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
            int titlePos = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            int originalTitlePos = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
            int overviewPos = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            int posterPos = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
            int backdropPos = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
            int votePos = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            int releasePos = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

            cursor.moveToPosition(i);

            MovieSearchResult movie = new MovieSearchResult();
            movie.setId(cursor.getLong(idPos));
            movie.setTitle(cursor.getString(titlePos));
            movie.setOriginalTitle(cursor.getString(originalTitlePos));
            movie.setOverview(cursor.getString(overviewPos));
            movie.setPosterPath(cursor.getString(posterPos));
            movie.setBackdropPath(cursor.getString(backdropPos));
            movie.setVoteAverage(cursor.getFloat(votePos));
            movie.setReleaseDate(cursor.getString(releasePos));
            movies.add(movie);
        }
        return movies;
    }

    private boolean isYoutubeTrailer(MovieVideo mv) {
        return mv.getType().equals(MOVIE_TYPE_TRAILER) && mv.getSite().equals(MOVIE_SITE_YOUTUBE);
    }

}
