package com.psincraian.movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.psincraian.movies.detailed.DetailedMovie;
import com.psincraian.movies.domain.Movie;
import com.psincraian.movies.movies.MoviesAdapter;
import com.psincraian.movies.utilities.SelectMoviesDialog;
import com.psincraian.movies.utilities.TmdbApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.ListItemClickListener,
        SelectMoviesDialog.SelectMoviesListener {

    private RecyclerView mListMovies;
    private MoviesAdapter mAdapter;
    private ProgressBar mProgressBar;
    private int mMoviesFilter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListMovies = (RecyclerView) findViewById(R.id.rv_list_movies);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        final int columns = getResources().getInteger(R.integer.movies_per_row);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columns);
        mListMovies.setLayoutManager(gridLayoutManager);
        mListMovies.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        mListMovies.setAdapter(mAdapter);
        onMovieSelected(SelectMoviesDialog.POPULAR_MOVIES);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailedMovie.class);
        intent.putExtra(DetailedMovie.EXTRA_MODEL, mAdapter.get(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.select_movies:
                SelectMoviesDialog.newInstance(this).show(getFragmentManager(), null);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(int option) {
        if (!isOnline()) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), R.string.error_no_internet_connection, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    })
                    .show();
        }

        if (option != mMoviesFilter) {
            mMoviesFilter = option;

            switch (mMoviesFilter) {
                case SelectMoviesDialog.POPULAR_MOVIES:
                    new QueryTask().execute(TmdbApi.POPULAR_MOVIES);
                    break;
                case SelectMoviesDialog.TOP_MOVIES:
                    new QueryTask().execute(TmdbApi.TOP_MOVIES);
                    break;
            }
        }
    }

    void updateList(List<Movie> movies) {
        mAdapter = new MoviesAdapter(movies, this);
        mListMovies.setAdapter(mAdapter);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class QueryTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListMovies.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... urls) {
            try {
                return TmdbApi.getMovies(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            updateList(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
            mListMovies.setVisibility(View.VISIBLE);
        }
    }

}
