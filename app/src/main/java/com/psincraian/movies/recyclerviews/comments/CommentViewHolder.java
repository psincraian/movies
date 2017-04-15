package com.psincraian.movies.recyclerviews.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.psincraian.movies.R;

class CommentViewHolder extends RecyclerView.ViewHolder {

    TextView author;
    TextView content;

    CommentViewHolder(View itemView) {
        super(itemView);

        author = (TextView) itemView.findViewById(R.id.tv_author);
        content = (TextView) itemView.findViewById(R.id.tv_content);
    }
}
