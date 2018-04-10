package it.and.stez78.popularmovies.app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import it.and.stez78.popularmovies.R;
import it.and.stez78.popularmovies.app.adapter.ElementReviewAdapter;
import it.and.stez78.popularmovies.app.adapter.ElementTrailerAdapter;
import it.and.stez78.popularmovies.app.adapter.OnItemClickListener;
import it.and.stez78.popularmovies.app.viewmodel.DetailsActivityViewModel;
import it.and.stez78.popularmovies.app.viewmodel.ViewModelFactory;
import it.and.stez78.popularmovies.model.MovieSearchResult;
import it.and.stez78.popularmovies.model.MovieVideo;
import it.and.stez78.popularmovies.utils.TheMovieDbUtils;

public class DetailsActivity extends AppCompatActivity implements OnItemClickListener {

    public static final String MOVIE_KEY_LABEL = "movieParcelable";

    @BindView(R.id.details_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.details_activity_background_image)
    ImageView background;

    @BindView(R.id.details_activity_poster_image)
    ImageView poster;

    @BindView(R.id.details_activity_title)
    TextView title;

    @BindView(R.id.details_activity_release_date)
    TextView releaseDate;

    @BindView(R.id.details_activity_rating_tv)
    TextView rating;

    @BindView(R.id.details_activity_rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.details_activity_overview)
    TextView overview;

    @BindView(R.id.details_activity_trailers_label)
    TextView trailersLabel;

    @BindView(R.id.details_activity_trailer_rv)
    RecyclerView trailerRv;

    @BindView(R.id.details_activity_reviews_label)
    TextView reviewsLabel;

    @BindView(R.id.details_activity_reviews_rv)
    RecyclerView reviewsRv;

    @BindView(R.id.details_activity_is_not_favorite_fab)
    FloatingActionButton notFavoriteFab;

    @BindView(R.id.details_activity_is_favorite_fab)
    FloatingActionButton isFavoriteFab;

    @Inject
    ViewModelFactory viewModelFactory;
    DetailsActivityViewModel viewModel;

    @Inject
    Picasso p;

    private ActionBar actionBar;
    private ElementTrailerAdapter trailerAdapter;
    private ElementReviewAdapter reviewAdapter;
    private boolean favoriteFirstLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsActivityViewModel.class);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent() != null && getIntent().hasExtra(MOVIE_KEY_LABEL)) {
            viewModel.initViewModel(getIntent().getParcelableExtra(MOVIE_KEY_LABEL));
            populateViews(viewModel.getMovie());
            setUpRecyclerViews();
            setUpFavorite();
        } else {
            finish();
        }
    }

    private void setUpRecyclerViews() {
        viewModel.getYoutubeTrailersLiveData().observe(this, trailers -> {
            if (trailers != null) {
                trailerRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                trailerAdapter = new ElementTrailerAdapter(trailers, p, this);
                trailerRv.setAdapter(trailerAdapter);
            }
        });

        viewModel.getReviewsLiveData().observe(this, reviews -> {
            if (reviews != null) {
                reviewsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                reviewAdapter = new ElementReviewAdapter(reviews);
                reviewsRv.setAdapter(reviewAdapter);
                reviewsRv.setNestedScrollingEnabled(false);
            }
        });

    }

    private void setUpFavorite() {
        viewModel.getIsFavoriteLiveData().observe(this, isFavorite -> {
            if (isFavorite) {
                if (!favoriteFirstLoading) {
                    Toast.makeText(this, getString(R.string.favorite_added, viewModel.getMovie().getTitle()), Toast.LENGTH_SHORT).show();
                }
                notFavoriteFab.setVisibility(View.INVISIBLE);
                isFavoriteFab.setVisibility(View.VISIBLE);
            } else {
                if (!favoriteFirstLoading) {
                    Toast.makeText(this, getString(R.string.favorite_removed, viewModel.getMovie().getTitle()), Toast.LENGTH_SHORT).show();
                }
                notFavoriteFab.setVisibility(View.VISIBLE);
                isFavoriteFab.setVisibility(View.INVISIBLE);
            }
            favoriteFirstLoading = false;
        });
    }

    private void populateViews(MovieSearchResult el) {
        p.load(Uri.parse(TheMovieDbUtils.getBackdropUrl(this, el, true))).fit().into(background);
        p.load(Uri.parse(TheMovieDbUtils.getPosterUrl(this, el))).into(poster);
        if (actionBar != null) actionBar.setTitle("");
        title.setText(el.getOriginalTitle());
        releaseDate.setText(el.getReleaseDate());
        rating.setText(getString(R.string.average_rating, el.getVoteAverage()));
        ratingBar.setRating(el.getVoteAverage() / 2);
        overview.setText(el.getOverview());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Object item) {
        if (item instanceof MovieVideo) {
            MovieVideo video = (MovieVideo) item;
            Intent playVideo = new Intent(Intent.ACTION_VIEW, Uri.parse(TheMovieDbUtils.getYoutubeMovieVideoUrl(video)));
            startActivity(playVideo);
        }
    }

    @OnClick({R.id.details_activity_is_not_favorite_fab, R.id.details_activity_is_favorite_fab})
    public void toggleFav() {
        viewModel.toggleFavorite();
    }

}
