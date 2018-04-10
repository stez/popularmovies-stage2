package it.and.stez78.popularmovies.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.and.stez78.popularmovies.R;
import it.and.stez78.popularmovies.model.MovieReview;

/**
 * Created by stefano on 11/03/18.
 */

public class ElementReviewAdapter extends RecyclerView.Adapter<ElementReviewAdapter.ViewHolder> {
    private static final String TAG = "ElReviewAdapter";

    private List<MovieReview> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView content;

        public ViewHolder(View v) {
            super(v);
            author = v.findViewById(R.id.list_element_review_author);
            content = v.findViewById(R.id.list_element_review_content);
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getContent() {
            return content;
        }
    }

    public ElementReviewAdapter(List<MovieReview> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_element_review, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MovieReview el = mDataSet.get(position);
        viewHolder.getAuthor().setText(el.getAuthor());
        viewHolder.getContent().setText(el.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
