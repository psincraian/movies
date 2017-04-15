package com.psincraian.movies.recyclerviews.comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.psincraian.movies.R;
import com.psincraian.movies.domain.Review;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private List<Review> reviews;

    public CommentsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.comment_view_holder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
