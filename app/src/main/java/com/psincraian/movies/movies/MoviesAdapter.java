package com.psincraian.movies.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.psincraian.movies.R;
import com.psincraian.movies.domain.Movie;
import com.psincraian.movies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter  extends RecyclerView.Adapter<MovieViewHolder> {

    private List<Movie> movies;
    private Context context;
    final private ListItemClickListener listener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MoviesAdapter(List<Movie> movies, ListItemClickListener listener)  {
        this.listener = listener;
        this.movies = new ArrayList<>(movies);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_view_holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        viewHolder.listener = listener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String url = movies.get(position).getPosterUrl();
        url = TmdbApi.buildImageUrl(url);
        Picasso.with(context).load(url).into(holder.poster);
        holder.title.setText(movies.get(position).getTitle());
        String rating = String.valueOf(movies.get(position).getRating());
        holder.rating.setText(rating);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Movie get(int pos) {
        return movies.get(pos);
    }
}
