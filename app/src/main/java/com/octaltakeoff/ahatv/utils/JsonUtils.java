package com.octaltakeoff.ahatv.utils;

import com.octaltakeoff.ahatv.data.MovieData;
import com.octaltakeoff.ahatv.model.Movie;
import com.octaltakeoff.ahatv.model.Review;
import com.octaltakeoff.ahatv.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String Tag = JsonUtils.class.getSimpleName();

    public static ArrayList<Movie> parseMoviesJason(String json) {

        try {

            JSONObject movieJsonObject = new JSONObject(json);
            JSONArray movieJsonArray = movieJsonObject.getJSONArray(MovieData.API_SEARCH_RESULTS);  // Refer to JSON file for the key name "results"

            ArrayList<Movie> movieItems = new ArrayList<>();
            for (int i = 0; i < movieJsonArray.length(); i++) {

                JSONObject currentMovieItem = movieJsonArray.getJSONObject(i);
                Movie movie = new Movie();
                // String "name" represents the actual API's String key. Refer and inspect JSON file for corresponding  key names.
                movie.setMovieId(currentMovieItem.getInt(MovieData.API_MOVIE_ID));
                movie.setPosterUrl(currentMovieItem.getString(MovieData.API_POSTER_PATH));
                movie.setBackdropPath(currentMovieItem.getString(MovieData.API_BACKDROP_PATH));
                movie.setMovieTitle(currentMovieItem.getString(MovieData.API_MOVIE_TITLE));
                movie.setReleaseDate(currentMovieItem.getString(MovieData.API_RELEASE_DATE));
                movie.setVoteAverage(currentMovieItem.getString(MovieData.API_AVERAGE_VOTE));
                movie.setPopularity(currentMovieItem.getString(MovieData.API_POPULARITY));
                movie.setSynopsis(currentMovieItem.getString(MovieData.API_OVERVIEW));
                movie.setOriginalLanguage(currentMovieItem.getString(MovieData.API_ORIGINAL_LANGUAGE));

                movieItems.add(movie);
            }
            return movieItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Review> parseReviewsJson(String json) {

        try {
            Review review;

            JSONObject reviewJsonObject = new JSONObject(json);
            JSONArray reviewJsonArray = reviewJsonObject.getJSONArray(MovieData.API_SEARCH_RESULTS);  // Refer to JSON file for the key name "results"

            ArrayList<Review> reviewItems = new ArrayList<>();
            for (int i = 0; i < reviewJsonArray.length(); i++) {

                JSONObject currentReviewItem = reviewJsonArray.getJSONObject(i);
                review = new Review();
                // String "name" represents the actual API's String key. Refer and inspect JSON file for corresponding  key names.
                review.setAuthor(currentReviewItem.getString("author"));
                review.setContent(currentReviewItem.getString("content"));
                review.setReviewId(currentReviewItem.getString("id"));
                review.setUrl(currentReviewItem.getString("url"));

                reviewItems.add(review);
            }
            return reviewItems;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Trailer> parseTrailersJson(String json) {
        try {
            Trailer trailer;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = new JSONArray(jsonObject.optString("results", "[\"]"));

            ArrayList<Trailer> trailerItems = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String currentItem = jsonArray.optString(i, "");
                JSONObject currentTrailerItem = new JSONObject(currentItem);

                trailer = new Trailer(
                        currentTrailerItem.optString("name", "Not Available"),
                        currentTrailerItem.optString("site", "Not Available"),
                        currentTrailerItem.optString("key", "Not Available")
                );
                trailerItems.add(trailer);
            }
            return trailerItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
