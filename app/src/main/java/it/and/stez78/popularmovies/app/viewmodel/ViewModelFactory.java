package it.and.stez78.popularmovies.app.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import it.and.stez78.popularmovies.app.repository.MoviesRepository;

/**
 * Created by stefano on 23/02/18.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MoviesRepository moviesRepository;

    @Inject
    public ViewModelFactory(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(moviesRepository);
        }
        if (modelClass.isAssignableFrom(DetailsActivityViewModel.class)) {
            return (T) new DetailsActivityViewModel(moviesRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}