package it.and.stez78.popularmovies.app.adapter;

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
import it.and.stez78.popularmovies.model.MovieVideo;
import it.and.stez78.popularmovies.utils.TheMovieDbUtils;

/**
 * Created by stefano on 05/03/18.
 */

public class ElementTrailerAdapter extends RecyclerView.Adapter<ElementTrailerAdapter.ViewHolder> {
    private static final String TAG = "ElTrailerAdapter";

    private List<MovieVideo> mDataSet;
    private Picasso p;
    private OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private ImageView poster;
        private ProgressBar progressBar;
        private ImageView errorIcon;
        private TextView trailerTitle;
        private ImageView play;

        public ViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.list_element_trailer_container);
            poster = v.findViewById(R.id.list_element_trailer_imageview);
            progressBar = v.findViewById(R.id.list_element_trailer_progbar);
            errorIcon = v.findViewById(R.id.list_element_trailer_error);
            trailerTitle = v.findViewById(R.id.list_element_trailer_title);
            play = v.findViewById(R.id.list_element_trailer_play_imageview);
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

        public TextView getTrailerTitle() {
            return trailerTitle;
        }

        public ImageView getPlay() {
            return play;
        }

        public void bindItemClickListener(MovieVideo el, OnItemClickListener listener) {
            container.setOnClickListener(v -> listener.onItemClick(el));
        }
    }

    public ElementTrailerAdapter(List<MovieVideo> dataSet, Picasso p, OnItemClickListener listener) {
        mDataSet = dataSet;
        this.p = p;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_element_trailer, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MovieVideo el = mDataSet.get(position);
        viewHolder.getProgressBar().setVisibility(View.VISIBLE);
        viewHolder.getPlay().setVisibility(View.INVISIBLE);
        p.load(Uri.parse(TheMovieDbUtils.getVideoThumbnailUrl(el.getKey())))
                .into(viewHolder.getPoster(), new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.getProgressBar().setVisibility(View.GONE);
                        viewHolder.getPoster().setVisibility(View.VISIBLE);
                        viewHolder.getPlay().setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        viewHolder.getProgressBar().setVisibility(View.GONE);
                        viewHolder.getErrorIcon().setVisibility(View.VISIBLE);
                    }
                });
        viewHolder.getTrailerTitle().setText(el.getName());
        viewHolder.getTrailerTitle().setVisibility(View.VISIBLE);
        viewHolder.bindItemClickListener(el, listener);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
