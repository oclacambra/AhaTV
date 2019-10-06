package com.octaltakeoff.ahatv.utils;

import android.net.Uri;

import com.octaltakeoff.ahatv.data.MovieData;
import com.octaltakeoff.ahatv.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    /**
     * Builds the URL
     */
    public static URL buildUrl(String sortCategory, int mPage) {

        Uri builtUri = Uri.parse(MovieData.BASE_URL).buildUpon()
                .appendPath(sortCategory)
                .appendQueryParameter(MovieData.API_PARAM, MovieData.API_KEY)
                .appendQueryParameter("page", String.valueOf(mPage))
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewUrl(int id, String reviewSearchQuery) {

        Uri builtUri = Uri.parse(MovieData.BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(reviewSearchQuery)
                .appendQueryParameter(MovieData.API_PARAM, MovieData.API_KEY)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Builds  movieImageURL
    public static URL buildMovieImageUrl(String imagePath) {

        Uri builtUri = Uri.parse(MovieData.POSTER_URL).buildUpon()
                .appendEncodedPath(imagePath)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * method HTTP Response result
     */

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
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
}
