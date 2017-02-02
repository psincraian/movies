package com.psincraian.movies.utilities;

import android.net.Uri;

import com.psincraian.movies.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TmdbApi {

    private static final String CLASS_NAME = TmdbApi.class.getSimpleName();
    private static final String API_KEY = "YOUT API HERE";

    public static final String TOP_MOVIES = "movie/top_rated";
    public static final String POPULAR_MOVIES = "movie/popular";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY_QUERY = "api_key";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String SMALL_IMAGE = "w342";

    public static List<Movie> getMovies(String section) throws Exception {
        URL url = buildUrl(section);
        String response = getResponseFromHttpUrl(url);
        return parseString(response);
    }

    public static String buildImageUrl(String resource) {
        Uri builtUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(SMALL_IMAGE)
                .appendEncodedPath(resource)
                .build();
        return builtUri.toString();
    }

    private static URL buildUrl(String path) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static List<Movie> parseString(final String data) throws Exception {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonResult = new JSONObject(data);
            JSONArray jsonMovies = jsonResult.getJSONArray("results");
            for (int i = 0; i < jsonMovies.length(); ++i) {
                JSONObject jsonMovie = jsonMovies.getJSONObject(i);

                Movie movie = new Movie();
                movie.setTitle(jsonMovie.getString("original_title"));
                movie.setPosterUrl(jsonMovie.getString("poster_path"));
                movie.setSynopsis(jsonMovie.getString("overview"));
                movie.setReleaseDate(jsonMovie.getString("release_date"));
                movie.setRating(jsonMovie.getDouble("vote_average"));

                movies.add(movie);
            }

        } catch (JSONException e) {
            throw new Exception("APSOKdpasok");
        }

        return movies;
    }
}
