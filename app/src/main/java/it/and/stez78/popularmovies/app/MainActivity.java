package it.and.stez78.popularmovies.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import it.and.stez78.popularmovies.R;
import it.and.stez78.popularmovies.app.adapter.ElementPosterAdapter;
import it.and.stez78.popularmovies.app.adapter.OnItemClickListener;
import it.and.stez78.popularmovies.app.viewmodel.MainActivityViewModel;
import it.and.stez78.popularmovies.app.viewmodel.ViewModelFactory;
import it.and.stez78.popularmovies.model.MovieSearchResult;

import static it.and.stez78.popularmovies.app.viewmodel.MainActivityViewModel.FAVORITES_POSITION;
import static it.and.stez78.popularmovies.app.viewmodel.MainActivityViewModel.MOST_POPULAR_POSITION;
import static it.and.stez78.popularmovies.app.viewmodel.MainActivityViewModel.TOP_RATED_POSITION;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String LAYOUT_MANAGER_STATE = "layoutManagerState";

    @BindView(R.id.main_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_activity_rv)
    RecyclerView recyclerView;

    @BindView(R.id.main_activity_bottom_nav)
    BottomNavigationView bottomNavigationView;

    private ElementPosterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Inject
    ViewModelFactory viewModelFactory;
    MainActivityViewModel viewModel;

    @Inject
    Picasso p;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAYOUT_MANAGER_STATE, layoutManager.onSaveInstanceState());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class);
        setSupportActionBar(toolbar);
        setUpBottomNavigation();
        setUpRecyclerView();
        if (viewModel.getMovies().isEmpty()) loadMovies(false);
        else if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE)) {
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel.isBottomSelectionOnFavorites()) {
            loadMovies(false);
        }
    }

    private void setUpRecyclerView() {
        int gridColumns = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridColumns = 3;
        }
        layoutManager = new StaggeredGridLayoutManager(gridColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ElementPosterAdapter(viewModel.getMovies(), p, this);
        recyclerView.setAdapter(adapter);
        viewModel.getMovieSearchResultLiveData().observe(this, list -> {
            viewModel.setMovies(new ArrayList<>());
            adapter.notifyDataSetChanged();
            if (list == null) {
                Toast.makeText(this, getString(R.string.internet_connection_error_msg), Toast.LENGTH_LONG).show();
            } else {
                viewModel.setMovies(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                recyclerView.smoothScrollToPosition(0);
                int id = item.getItemId();
                switch (id) {
                    case R.id.bottom_top_rated:
                        viewModel.setBottomSelection(TOP_RATED_POSITION);
                        break;
                    case R.id.bottom_most_popular:
                        viewModel.setBottomSelection(MOST_POPULAR_POSITION);
                        break;
                    case R.id.bottom_favorites:
                        viewModel.setBottomSelection(FAVORITES_POSITION);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            loadMovies(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(final boolean scrollToTop) {
        viewModel.setBottomSelection(getBottomSelection());
        if (scrollToTop) recyclerView.smoothScrollToPosition(0);
    }

    private int getBottomSelection() {
        switch (bottomNavigationView.getSelectedItemId()) {
            case R.id.bottom_top_rated:
                return TOP_RATED_POSITION;
            case R.id.bottom_most_popular:
                return MOST_POPULAR_POSITION;
            case R.id.bottom_favorites:
                return FAVORITES_POSITION;
            default:
                return MOST_POPULAR_POSITION;
        }
    }

    @Override
    public void onItemClick(Object item) {
        Intent detailsActivity = new Intent(this, DetailsActivity.class);
        detailsActivity.putExtra(DetailsActivity.MOVIE_KEY_LABEL, ((MovieSearchResult) item));
        startActivity(detailsActivity);
    }
}
