package it.and.stez78.popularmovies.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import it.and.stez78.popularmovies.R;
import it.and.stez78.popularmovies.model.MovieSearchResult;
import it.and.stez78.popularmovies.utils.TheMovieDbUtils;

/**
 * Created by stefano on 19/02/18.
 */

public class ElementPosterAdapter extends RecyclerView.Adapter<ElementPosterAdapter.ViewHolder> {
    private static final String TAG = "ElPosterAdapter";

    private Context context;
    private List<MovieSearchResult> mDataSet;
    private Picasso p;
    private OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private ImageView poster;
        private ProgressBar progressBar;
        private ImageView errorIcon;
        private TextView title;

        public ViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.list_element_poster_container);
            poster = v.findViewById(R.id.list_element_poster_imageview);
            progressBar = v.findViewById(R.id.list_element_poster_progbar);
            errorIcon = v.findViewById(R.id.list_element_poster_error);
            title = v.findViewById(R.id.list_element_poster_title);
        }

        public ImageView getPoster() {
            return poster;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public ImageView getErrorIcon() {
            return errorIcon;
        }

        public TextView getTitle() {
            return title;
        }

        public void bindItemClickListener(MovieSearchResult el, OnItemClickListener listener) {
            container.setOnClickListener(v -> listener.onItemClick(el));
        }
    }

    public ElementPosterAdapter(List<MovieSearchResult> dataSet, Picasso p, OnItemClickListener listener) {
        mDataSet = dataSet;
        this.p = p;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_element_poster, viewGroup, false);
        context = v.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MovieSearchResult el = mDataSet.get(position);
        viewHolder.getProgressBar().setVisibility(View.VISIBLE);
        p.load(Uri.parse(TheMovieDbUtils.getPosterUrl(context, el)))
                .into(viewHolder.getPoster(), new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.getProgressBar().setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        viewHolder.getProgressBar().setVisibility(View.GONE);
                        viewHolder.getErrorIcon().setVisibility(View.VISIBLE);
                        viewHolder.getTitle().setText(el.getTitle());

                    }
                });
        viewHolder.bindItemClickListener(el, listener);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}