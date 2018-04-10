package it.and.stez78.popularmovies.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import it.and.stez78.popularmovies.PopularMoviesApplication;

/**
 * Created by stefano on 18/02/18.
 */


@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ActivitiesModule.class,
        AppModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(PopularMoviesApplication popularMoviesApplication);
}