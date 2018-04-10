package it.and.stez78.popularmovies.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.and.stez78.popularmovies.app.repository.MoviesRepository;
import it.and.stez78.popularmovies.model.MovieSearchResult;

/**
 * Created by stefano on 23/02/18.
 */

public class MainActivityViewModel extends ViewModel {

    public static final int MOST_POPULAR_POSITION = 0;
    public static final int TOP_RATED_POSITION = 1;
    public static final int FAVORITES_POSITION = 2;

    private List<MovieSearchResult> movies = new ArrayList<>();
    private MoviesRepository moviesRepository;
    private LiveData<List<MovieSearchResult>> movieSearchResultLiveData;
    private MutableLiveData<Integer> bottomSelectionLiveData = new MutableLiveData<>();

    @Inject
    public MainActivityViewModel(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
        this.movieSearchResultLiveData = Transformations.switchMap(bottomSelectionLiveData, selection -> {
            switch (selection) {
                case MOST_POPULAR_POSITION:
                    return moviesRepository.getPopular();
                case TOP_RATED_POSITION:
                    return moviesRepository.getTopRated();
                case FAVORITES_POSITION:
                    return moviesRepository.getFavorite();
                default:
                    return moviesRepository.getPopular();
            }
        });
    }

    public void setBottomSelection(int bottomSelection) {
        bottomSelectionLiveData.setValue(bottomSelection);
    }

    public LiveData<List<MovieSearchResult>> getMovieSearchResultLiveData() {
        return movieSearchResultLiveData;
    }

    public List<MovieSearchResult> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieSearchResult> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
    }

    public boolean isBottomSelectionOnFavorites() {
        return bottomSelectionLiveData.getValue().equals(FAVORITES_POSITION);
    }
}
