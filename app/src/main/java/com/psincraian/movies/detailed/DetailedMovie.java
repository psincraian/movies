package com.psincraian.movies.detailed;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.psincraian.movies.R;
import com.psincraian.movies.domain.Movie;
import com.psincraian.movies.utilities.TmdbApi;
import com.squareup.picasso.Picasso;

public class DetailedMovie extends AppCompatActivity {

    public static final String EXTRA_MODEL = "extra_model";

    private ImageView poster;
    private TextView synopsis;
    private TextView rating;
    private TextView releaseDate;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rating = (TextView) findViewById(R.id.tv_movie_rating);
        releaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        synopsis = (TextView) findViewById(R.id.tv_movie_synopsis);
        poster = (ImageView) findViewById(R.id.main_backdrop);

        Bundle extra = getIntent().getExtras();
        Movie movie = extra.getParcelable(EXTRA_MODEL);
        getSupportActionBar().setTitle(movie.getTitle());
        rating.setText(String.valueOf(movie.getRating()));
        releaseDate.setText(movie.getReleaseDate());
        synopsis.setText(movie.getSynopsis());
        String url = TmdbApi.buildImageUrl(movie.getPosterUrl());
        Picasso.with(this).load(url).fit().centerCrop().into(poster);
    }
}
