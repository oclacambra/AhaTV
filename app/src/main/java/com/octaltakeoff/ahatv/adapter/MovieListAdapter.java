package com.octaltakeoff.ahatv.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.octaltakeoff.ahatv.R;
import com.octaltakeoff.ahatv.model.Movie;
import com.octaltakeoff.ahatv.ui.MovieDetailsActivity;
import com.octaltakeoff.ahatv.utils.ColumnUtils;
import com.octaltakeoff.ahatv.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private static final String TAG = "MovieListAdapter";

    //final private MovieItemClickListener onClickListener;
    private LayoutInflater inflater;
    //List<Movie> mMovie = Collections.emptyList();
    List<Movie> mMovie;
    //private Context context;
    Activity mActivity;
    private int iconWidth;
    public int mNoOfColumns;

    /**
     * The interface that receives onclick messages.
     */

//    public interface MovieItemClickListener {
//        void onMovieItemClick(Movie movieItem);
//    }

    /**
     * Default Constructor
     */

    public MovieListAdapter(Activity activity, List<Movie> movie) {
        //context = mContext;
        mMovie = movie;
        mActivity = activity;
        inflater = LayoutInflater.from(mActivity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movielist_adapter_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder : called.");


        // Change the size of the thumbnails
        mNoOfColumns = (int) Math.floor(ColumnUtils.calculateNoOfColumns(mActivity));
        iconWidth = (int) Math.floor(ColumnUtils.calculateIconWidth(mActivity, mNoOfColumns));
        int iconHeight = (int) (iconWidth * 1.41);
        int margin = 12;

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) holder.parentLayout.getLayoutParams();
        params.width = iconWidth;
        params.height = iconHeight;
        ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) holder.parentLayout.getLayoutParams();
        params2.setMargins(12,12,12,12);
        holder.parentLayout.setLayoutParams(params2);



        final Movie currentMovie = mMovie.get(position);
        URL posterUrl = NetworkUtils.buildMovieImageUrl(currentMovie.getPosterUrl());

        final String currentMovieTitle = currentMovie.getMovieTitle();
        final String currentReleaseDate = currentMovie.getReleaseDate();

        Picasso.with(mActivity)
                .load(posterUrl.toString())
                .into(holder.posterUrl_imageView);

        //holder.movieTitle_textView.setText(currentMovieTitle);
        //holder.releaseDate_textView.setText(currentReleaseDate);

        // Set onclick listener to the parent layout
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = currentMovie.getMovieId();

                Intent intent = new Intent(mActivity, MovieDetailsActivity.class);
                intent.putExtra("movieId", id);
                intent.putExtra("movieItem", currentMovie);

                // for shared element transition animation
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, (View) view, "profile");

                // add options to bundle for transition animation
                mActivity.startActivity(intent, options.toBundle());

            }
        });

    }

    public void setMovieDetails(List<Movie> movie) {
        mMovie = movie;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMovie.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView posterUrl_imageView;
        TextView movieTitle_textView;
        TextView releaseDate_textView;
        CardView parentLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            posterUrl_imageView = itemView.findViewById(R.id.iv_moviePoster);
            movieTitle_textView = itemView.findViewById(R.id.tv_movieTitle);
            releaseDate_textView = itemView.findViewById(R.id.tv_releaseDate);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
