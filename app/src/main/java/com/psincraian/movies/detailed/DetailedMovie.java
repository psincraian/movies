package com.psincraian.movies.detailed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.psincraian.movies.R;
import com.psincraian.movies.domain.Review;
import com.psincraian.movies.domain.Movie;
import com.psincraian.movies.recyclerviews.comments.CommentsAdapter;
import com.psincraian.movies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailedMovie extends AppCompatActivity {

    public static final String EXTRA_MODEL = "extra_model";
    private RecyclerView reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView rating = (TextView) findViewById(R.id.tv_movie_rating);
        TextView releaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        TextView synopsis = (TextView) findViewById(R.id.tv_movie_synopsis);
        ImageView poster = (ImageView) findViewById(R.id.main_backdrop);
        reviews = (RecyclerView) findViewById(R.id.rv_list_comments);
        reviews.setLayoutManager(new LinearLayoutManager(this));
        reviews.setAdapter(new CommentsAdapter(new ArrayList<Review>()));

        Bundle extra = getIntent().getExtras();
        Movie movie = extra.getParcelable(EXTRA_MODEL);
        getSupportActionBar().setTitle(movie.getTitle());
        rating.setText(String.valueOf(movie.getRating()));
        releaseDate.setText(movie.getReleaseDate());
        synopsis.setText(movie.getSynopsis());
        String url = TmdbApi.buildImageUrl(movie.getPosterUrl());
        Picasso.with(this).load(url).fit().centerCrop().into(poster);

        new QueryTask().execute(movie.getId());
    }

    void updateList(List<Review> reviews) {
        CommentsAdapter adapter = new CommentsAdapter(reviews);

        this.reviews.setAdapter(adapter);
    }

    private class QueryTask extends AsyncTask<Integer, Void, List<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reviews.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<Review> doInBackground(Integer... movieId) {
            try {
                return TmdbApi.getReviews(movieId[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Review> res) {
            super.onPostExecute(res);
            updateList(res);
            reviews.setVisibility(View.VISIBLE);
        }
    }
}
