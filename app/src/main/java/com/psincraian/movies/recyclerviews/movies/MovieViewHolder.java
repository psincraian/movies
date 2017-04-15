package com.psincraian.movies.recyclerviews.movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.psincraian.movies.R;
import com.psincraian.movies.recyclerviews.ListItemClickListener;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView poster;
    TextView title;
    TextView rating;
    ListItemClickListener listener;

    public MovieViewHolder(View itemView) {
        super(itemView);

        poster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        title = (TextView) itemView.findViewById(R.id.tv_movie_title);
        rating = (TextView) itemView.findViewById(R.id.tv_movie_rating);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int pos = getAdapterPosition();
        listener.onListItemClick(pos);
    }
}
