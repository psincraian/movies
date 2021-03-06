package com.psincraian.movies.utilities;

import android.net.Uri;
import android.util.Log;

import com.psincraian.movies.domain.Movie;
import com.psincraian.movies.domain.Review;

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
    private static final String API_KEY = "8750eb6f8c8778c649a5fdf7fde047ce";

    public static final String TOP_MOVIES = "movie/top_rated";
    public static final String POPULAR_MOVIES = "movie/popular";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY_QUERY = "api_key";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String SMALL_IMAGE = "w342";
    private static final String MOVIE_PATH = "movie";
    private static final String REVIEW_PATH = "reviews";

    public static List<Movie> getMovies(String section) throws Exception {
        URL url = buildUrl(section);
        String response = getResponseFromHttpUrl(url);
        return parseMovieString(response);
    }

    public static List<Review> getReviews(int movieId) throws Exception {
        URL url = buildReviewUrl(movieId);
        String response = getResponseFromHttpUrl(url);
        return parseReviewString(response);
    }

    public static String buildImageUrl(String resource) {
        Uri builtUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(SMALL_IMAGE)
                .appendEncodedPath(resource)
                .build();
        return builtUri.toString();
    }

    private static URL buildReviewUrl(int movieId) {
        Uri builtUri= Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(movieId))
                .appendPath(REVIEW_PATH)
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;    }

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

    private static List<Movie> parseMovieString(final String data) throws Exception {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonResult = new JSONObject(data);
            JSONArray jsonMovies = jsonResult.getJSONArray("results");
            for (int i = 0; i < jsonMovies.length(); ++i) {
                JSONObject jsonMovie = jsonMovies.getJSONObject(i);

                Movie movie = new Movie();
                movie.setId(jsonMovie.getInt("id"));
                movie.setTitle(jsonMovie.getString("original_title"));
                movie.setPosterUrl(jsonMovie.getString("poster_path"));
                movie.setSynopsis(jsonMovie.getString("overview"));
                movie.setReleaseDate(jsonMovie.getString("release_date"));
                movie.setRating(jsonMovie.getDouble("vote_average"));

                movies.add(movie);
            }

        } catch (JSONException e) {
            throw new Exception(CLASS_NAME + "Error parsing json");
        }

        return movies;
    }

    private static List<Review> parseReviewString(final String data) throws Exception {
        List<Review> reviews = new ArrayList<>();

        try {
            JSONObject jsonResult = new JSONObject(data);
            Log.d(CLASS_NAME, jsonResult.toString());
            JSONArray jsonReviews = jsonResult.getJSONArray("results");
            for (int i = 0; i < jsonReviews.length(); ++i) {
                JSONObject jsonReview = jsonReviews.getJSONObject(i);

                Review review = new Review();
                review.setId(jsonReview.getString("id"));
                review.setAuthor(jsonReview.getString("author"));
                review.setContent(jsonReview.getString("content"));
                review.setMovieId(jsonResult.getInt("id"));

                reviews.add(review);
            }
        } catch (JSONException e) {
            throw new Exception(CLASS_NAME + "Error parsing json");
        }

        return reviews;
    }
}
