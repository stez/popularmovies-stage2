package it.and.stez78.popularmovies.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import it.and.stez78.popularmovies.app.DetailsActivity;
import it.and.stez78.popularmovies.app.MainActivity;

/**
 * Created by stefano on 19/02/18.
 */

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector
    abstract DetailsActivity contributeDetailsActivityInjector();
}