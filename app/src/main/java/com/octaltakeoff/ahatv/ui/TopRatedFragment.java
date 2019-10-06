package com.octaltakeoff.ahatv.ui;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.octaltakeoff.ahatv.R;
import com.octaltakeoff.ahatv.adapter.MovieListAdapter;
import com.octaltakeoff.ahatv.data.MovieData;
import com.octaltakeoff.ahatv.model.Movie;
import com.octaltakeoff.ahatv.utils.ColumnUtils;
import com.octaltakeoff.ahatv.utils.JsonUtils;
import com.octaltakeoff.ahatv.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatedFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "TopRatedFragment";

    // variables
    private TextView footerTextView;

    private RecyclerView movieList_RecyclerView;
    private RecyclerView.LayoutManager movieLayoutManager;
    private Parcelable state;
    private MovieListAdapter movieListAdapter;
    private String sortCategory;
    private String sortCategoryString;
    public ArrayList<Movie> movieList;
    public ArrayList<Movie> mMovieList = null;
    //    private List<MovieEntry> movieDbList;
    private int mNoOfColumns;
    TextView errorMessageTextView;
    ProgressBar mLoadingIndicator;
    Context context;
    Integer mPageNumber = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_layout, container, false);

        footerTextView = view.findViewById(R.id.footer_textView);
        footerTextView.setText("This is Top Rated Fragment");


        //getActivity().setTitle(getString(R.string.app_name) + " - " + sortCategory );

        movieList_RecyclerView = view.findViewById(R.id.recycleView_grid);
        //errorMessageTextView = view.findViewById(R.id.tv_error_message);
        //mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);

        sortCategoryString = getString(R.string.ADD_FAVORITE);

        mNoOfColumns = ColumnUtils.calculateNoOfColumns(getActivity());
        movieLayoutManager = new GridLayoutManager(getContext(), mNoOfColumns);

        movieListAdapter = new MovieListAdapter(getActivity(), movieList);


        //movieList.clear();
        sortCategory = MovieData.TOP_MOVIES_URL;
        sortCategoryString = getString(R.string.ADD_FAVORITE);
        //getActivity().setTitle(getString(R.string.app_name) + " - " + sortCategory );
        //loadMovies();


        //For Device orientation  -- correction for state of scroll position
        if (savedInstanceState != null) {
            //sortCategory = savedInstanceState.getString("SortCategory");
            //sortCategoryString = savedInstanceState.getString("SortCategoryString");
            state = savedInstanceState.getParcelable("KeyForGridLayoutManager");
            //getActivity().setTitle(getString(R.string.app_name) + " - " + sortCategory );
            loadMovies();

        } else {
//            if (isOnline()) {
//                setUpViewModel();
//            } else {
//                showErrorMessage();
//            }

            //getActivity().setTitle(getString(R.string.app_name) + " - " + sortCategory );
            loadMovies();
        }

        return view;
    }

    public void loadMovies() {

        String movieQuery = sortCategory;
        URL movieSearchUrl = NetworkUtils.buildUrl(movieQuery, 1);
        new MovieQueryTask().execute(movieSearchUrl);


    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        int size = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showLoadingIndicator();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults == null) {
                showErrorMessage();
                return;
            }
//            if (searchResults != null && !searchResults.equals("")) {
//                movieList = JsonUtils.parseMoviesJason(searchResults);
//                movieListAdapter.setMovieDetails(movieList);
//                initializeRecyclerView();
//                movieList_RecyclerView.setAdapter(movieListAdapter);
//                movieList_RecyclerView.getLayoutManager().onRestoreInstanceState(state); // correction for state of scroll position
//
//            }

            // Pagination that add the pages
            if (searchResults != null && !searchResults.equals("")) {
                if (movieList == null) {
                    movieList = JsonUtils.parseMoviesJason(searchResults);

                } else {
                    if (mMovieList == null) {
                        mMovieList = JsonUtils.parseMoviesJason(searchResults);
                        size = movieList.size();
                    } else {
                        mMovieList.clear();
                        //mMovieList = JsonUtils.parseMoviesJason(searchResults);
                    }

                    // Check if network is exhausted
                    //if(mMovieList.size() % 20 != 0 ){
                    if (mMovieList.size() < 20) {
                        movieList.remove(mMovieList);
                        movieListAdapter.notifyItemRangeChanged(movieList.size(), movieListAdapter.getItemCount());
                        //movieList_RecyclerView.scrollToPosition(movieList.size() - size);

                    } else {
                        movieList.addAll(mMovieList);
                        mMovieList.clear();
                    }
                }
                movieListAdapter.setMovieDetails(movieList);
                initializeRecyclerView();
                movieList_RecyclerView.setAdapter(movieListAdapter);
                //recycleViewState = movieList_RecyclerView.getLayoutManager().onSaveInstanceState();
                // movieList_RecyclerView.getLayoutManager().onRestoreInstanceState(recycleViewState); // correction for state of scroll position
                movieList_RecyclerView.scrollToPosition(movieList.size() - size);
            }
        }
    }


    public void showErrorMessage() {
        movieList_RecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public void showViewGrid() {
        movieList_RecyclerView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public void showLoadingIndicator() {
        movieList_RecyclerView.setVisibility(View.GONE);
        errorMessageTextView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void initializeRecyclerView() {

        movieLayoutManager = new GridLayoutManager(getActivity(), mNoOfColumns);
        movieList_RecyclerView.setLayoutManager(movieLayoutManager);

        // For pagination
        movieList_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // Check if the the scroll is at the bottom of the list
                if (!movieList_RecyclerView.canScrollVertically(1)) {
                    // search the next page

                    if (mPageNumber < 2 && movieList.size() < 40) {
                        searchNextPage();
                    } else {
                        //movieList_RecyclerView.scrollToPosition(mMovieList.size());
                    }
                }

            }
        });
    }

    //  For device orientation - correction for state of scroll position
    @Override
    public void onSaveInstanceState(@Nullable Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        //saveInstanceState.putString("SortCategory", sortCategory);
        //saveInstanceState.putString("SortCategoryString", sortCategoryString);
        saveInstanceState.putParcelable("KeyForGridLayoutManager", movieLayoutManager.onSaveInstanceState());
    }

    public void searchNextPage() {
        //mMovieList.clear();
        String movieQuery = sortCategory;
        URL movieSearchUrl = NetworkUtils.buildUrl(movieQuery, mPageNumber);
        new MovieQueryTask().execute(movieSearchUrl);

    }
}