package com.octaltakeoff.ahatv.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.octaltakeoff.ahatv.R;
import com.octaltakeoff.ahatv.adapter.TrailerAdapter;
import com.octaltakeoff.ahatv.data.MovieData;
import com.octaltakeoff.ahatv.model.Movie;
import com.octaltakeoff.ahatv.model.Review;
import com.octaltakeoff.ahatv.model.Trailer;
import com.octaltakeoff.ahatv.utils.JsonUtils;
import com.octaltakeoff.ahatv.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {
    public Movie movie;
    private ImageView backDropImageView;
    private ImageView posterImageView;
    private ImageView playVideoImageView;
    private TextView movieTitleTextView;
    private TextView averageVoteTextView;
    private TextView popularityTextView;
    private TextView origLanguageTextView;
    private TextView releaseDateTextView;
    private TextView synopsisTextView;
    private TextView reviewsTextView;
    private TextView trailerTextView;
    private Button fabMovieButton;
    private RatingBar ratingBar;

    private String sortCategoryString;
    private String sortCategory;

    //private MovieDataBase mDb;

    private int id;
    private String rating;
    private String movieTitle;
    private String popularity;
    private String posterPath;
    private String origLanguage;
    private String backdropPath;
    private String synopsis;
    private String releaseDate;

    private ArrayList<Trailer> trailerList;
    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<Review> reviewList;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        backDropImageView = (ImageView) findViewById(R.id.iv_backDropImage);
        posterImageView = (ImageView) findViewById(R.id.iv_moviePoster);
        playVideoImageView = (ImageView) findViewById(R.id.iv_playButton);
        movieTitleTextView = (TextView) findViewById(R.id.tv_movieTitle);
        averageVoteTextView = (TextView) findViewById(R.id.tv_averageVote);
        popularityTextView = (TextView) findViewById(R.id.tv_popularity);
        origLanguageTextView = (TextView) findViewById(R.id.tv_origLanguage);
        releaseDateTextView = (TextView) findViewById(R.id.tv_releaseDate);
        synopsisTextView = (TextView) findViewById(R.id.tv_synopsis);
        reviewsTextView = (TextView) findViewById(R.id.tv_reviews);
        trailerTextView = (TextView) findViewById(R.id.tv_trailer);
        fabMovieButton = (Button) findViewById(R.id.btn_addMovie);
        ratingBar = (RatingBar) findViewById(R.id.rbv_user_rating);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Display the home button

        reviewList = new ArrayList<Review>();

        // Get the intents

        Intent receiveIntent = getIntent();
        //sortCategory = receiveIntent.getStringExtra("sortCategory");
        //sortCategoryString = receiveIntent.getStringExtra("sortCategoryString");
        Bundle data = receiveIntent.getExtras();
        movie = (Movie) data.getSerializable("movieItem");

        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        mTrailerAdapter = new TrailerAdapter(this, trailerList, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(mLayoutManager);

        //mDb = MovieDataBase.getMovieDataBase(getApplicationContext());

        reviewList = new ArrayList<Review>();
        getMoreDetails(movie.getMovieId());
        populateUIDetails();

        playVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String id = trailerList.get(0).getKey();
                    if (trailerList.size() > 0) {
                        watchYouTubeVideo(id);
                    } else {
                        trailerTextView.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private static class SearchURLs {
        URL reviewSearchUrl;
        URL trailerSearchUrl;

        SearchURLs(URL reviewSearchUrl, URL trailerSearchUrl) {
            this.reviewSearchUrl = reviewSearchUrl;
            this.trailerSearchUrl = trailerSearchUrl;
        }
    }

    private static class ResultsStrings {
        String reviewString;
        String trailerString;

        ResultsStrings(String reviewString, String trailerString) {
            this.reviewString = reviewString;
            this.trailerString = trailerString;
        }
    }

    private void getMoreDetails(int id) {

        String reviewQuery = MovieData.API_MOVIE_REVIEWS;
        String trailerQuery = MovieData.API_MOVIE_TRAILERS;

        SearchURLs searchUrls = new SearchURLs(
                NetworkUtils.buildReviewUrl(id, reviewQuery),
                NetworkUtils.buildReviewUrl(id, trailerQuery)
        );
        new ReviewQueryTask().execute(searchUrls);
    }

    public class ReviewQueryTask extends AsyncTask<SearchURLs, Void, ResultsStrings> {

        @Override
        protected ResultsStrings doInBackground(SearchURLs... params) {
            URL reviewSearchUrl = params[0].reviewSearchUrl;
            URL trailerSearchUrl = params[0].trailerSearchUrl;

            String reviewResults = null;
            try {
                reviewResults = NetworkUtils.getResponseFromHttpUrl(reviewSearchUrl);
                reviewList = JsonUtils.parseReviewsJson(reviewResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String trailerResults = null;
            try {
                trailerResults = NetworkUtils.getResponseFromHttpUrl(trailerSearchUrl);
                trailerList = JsonUtils.parseTrailersJson(trailerResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResultsStrings resultsStrings = new ResultsStrings(reviewResults, trailerResults);
            return resultsStrings;

        }

        @Override
        protected void onPostExecute(ResultsStrings results) {
            String searchResults = results.reviewString;
            if (searchResults != null && !searchResults.equals("")) {
                reviewList = JsonUtils.parseReviewsJson(searchResults);
                displayReviews();
            }
        }
    }

    public void displayReviews() {

        if (trailerList.size() != 0 && !trailerList.equals(null)) {
            mTrailerAdapter.setTrailerData(trailerList);
            trailerTextView.setVisibility(View.GONE);
        } else {
            trailerTextView.setVisibility(View.VISIBLE);
            return;
        }

        if (reviewList.size() != 0 && !reviewList.equals(null)) {
            reviewsTextView.setText("");
            for (int i = 0; i < reviewList.size(); i++) {
                reviewsTextView.append("\n");
                reviewsTextView.append(reviewList.get(i).getContent());
                reviewsTextView.append("\n\n");
                reviewsTextView.append("Reviewed by :  \n");
                reviewsTextView.append("     " + reviewList.get(i).getAuthor());
                reviewsTextView.append("\n___________________________\n");
            }

        } else {
            reviewsTextView.setText(getString(R.string.review_not_available));
        }
    }

    public void populateUIDetails() {
        rating = movie.getVoteAverage();
        movieTitle = movie.getMovieTitle();
        popularity = movie.getPopularity();
        posterPath = movie.getPosterUrl();
        origLanguage = movie.getOriginalLanguage();
        backdropPath = movie.getBackdropPath();
        synopsis = movie.getSynopsis();
        releaseDate = movie.getReleaseDate();

        fabMovieButton.setText(sortCategoryString);

        if (backdropPath != null) {
            URL backDropUrl = NetworkUtils.buildMovieImageUrl(backdropPath);
            Picasso.with(getBaseContext())
                    .load((backDropUrl.toString()))
                    .into(backDropImageView);
        } else {
            backDropImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        if (posterPath != null) {
            URL posterUrl = NetworkUtils.buildMovieImageUrl(posterPath);
            Picasso.with(getBaseContext())
                    .load((posterUrl.toString()))
                    .into(posterImageView);
        } else {
            posterImageView.setImageResource(R.mipmap.ic_launcher);
        }

        if (movieTitle != null) {
            movieTitleTextView.setText(movieTitle);
            setTitle(movieTitle);
        }

        if (rating != null) {
            averageVoteTextView.setText(rating.toString());
            ratingBar.setRating(Float.parseFloat(String.valueOf(rating)));
        } else {
            averageVoteTextView.setText(getString(R.string.data_not_available));
        }

        if (popularity != null) {
            popularityTextView.setText(popularity.toString());
        } else {
            popularityTextView.setText(getString(R.string.data_not_available));
        }

        if (origLanguage != null) {
            origLanguageTextView.setText(origLanguage);
        } else {
            origLanguageTextView.setText(getString(R.string.data_not_available));
        }

        if (releaseDate != null) {
            releaseDateTextView.setText(releaseDate);
        } else {
            releaseDateTextView.setText(getString(R.string.data_not_available));
        }

        if (synopsis != null) {
            synopsisTextView.setText(synopsis);
        } else {
            synopsisTextView.setText(getString(R.string.data_not_available));
        }

//        fabMovieButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final MovieEntry movieEntry = new MovieEntry(id, rating, movieTitle, popularity, posterPath, origLanguage,
//                        backdropPath, synopsis, releaseDate);
//
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (sortCategory.equals(MovieData.FAVORITE_MOVIES)) {
//                            mDb.movieDao().deleteMovieById(movie.getMovieId());
//                            finish();
//                        } else {
//                            mDb.movieDao().InsertMovie(movieEntry);
//                            finish();
//                        }
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                sortCategory.equals(MovieData.POPULAR_MOVIES_URL);
//                            }
//                        });
//                    }
//                });
//            }
//        });
    }

    private void watchYouTubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        webIntent.putExtra("finish_on_ended", true);
        // correction for try catch block
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        }
        if (appIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(appIntent);
        }
    }

    @Override
    public void OnListItemClick(Trailer trailerItem) {
        watchYouTubeVideo(trailerItem.getKey());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Go back to last view with transition support
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        // Go to last view
        onBackPressed();
        return true;
    }


}
