package it.and.stez78.popularmovies.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import it.and.stez78.popularmovies.app.repository.MoviesRepository;
import it.and.stez78.popularmovies.model.MovieReview;
import it.and.stez78.popularmovies.model.MovieSearchResult;
import it.and.stez78.popularmovies.model.MovieVideo;

/**
 * Created by stefano on 05/03/18.
 */

public class DetailsActivityViewModel extends ViewModel {

    private MoviesRepository moviesRepository;
    private MovieSearchResult movie;
    private MutableLiveData<Boolean> isFavoriteLiveData = new MutableLiveData<>();
    private LiveData<List<MovieVideo>> youtubeTrailersLiveData;
    private LiveData<List<MovieReview>> reviewsLiveData;


    @Inject
    public DetailsActivityViewModel(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public void initViewModel(MovieSearchResult movie) {
        this.movie = movie;
        isFavoriteLiveData.setValue(moviesRepository.isFavorite(movie.getId()));
    }

    public LiveData<List<MovieVideo>> getYoutubeTrailersLiveData() {
        if (youtubeTrailersLiveData == null) {
            youtubeTrailersLiveData = moviesRepository.getYoutubeTrailers(movie.getId());
        }
        return youtubeTrailersLiveData;
    }

    public LiveData<List<MovieReview>> getReviewsLiveData() {
        if (reviewsLiveData == null) {
            reviewsLiveData = moviesRepository.getReviews(movie.getId());
        }
        return reviewsLiveData;
    }

    public LiveData<Boolean> getIsFavoriteLiveData() {
        return isFavoriteLiveData;
    }

    public void toggleFavorite() {
        boolean isFav = moviesRepository.isFavorite(movie.getId());
        if (isFav) {
            if (moviesRepository.removeFavorite(movie.getId())) {
                isFavoriteLiveData.setValue(false);
            }
        } else {
            if (moviesRepository.setFavorite(movie)) {
                isFavoriteLiveData.setValue(true);
            }
        }
    }

    public MovieSearchResult getMovie() {
        return movie;
    }

    public void setMovie(MovieSearchResult movie) {
        this.movie = movie;
    }
}
