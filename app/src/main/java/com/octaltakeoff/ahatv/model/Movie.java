package com.octaltakeoff.ahatv.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private int mMovieId;
    private String mVoteAverage;
    private String mMovieTitle;
    private String mPopularity;
    private String mPosterUrl;
    private String mOriginalLanguage;
    private String mBackdropPath;
    private String mSynopsis;
    private String mReleaseDate;

    /**
     * The main Constructor
     */
    public Movie(int movieId, String voteAverage, String movieTitle, String popularity, String posterUrl, String originalLanguage,
                 String backdropPath, String synopsis, String releaseDate) {
        this.mMovieId = movieId;
        this.mVoteAverage = voteAverage;
        this.mMovieTitle = movieTitle;
        this.mPopularity = popularity;
        this.mPosterUrl = posterUrl;
        this.mOriginalLanguage = originalLanguage;
        this.mBackdropPath = backdropPath;
        this.mSynopsis = synopsis;
        this.mReleaseDate = releaseDate;
    }

    public Movie() {

    }

    /**
     * Getter and Setter
     */

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        this.mMovieId = movieId;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.mMovieTitle = movieTitle;
    }

    public String getPopularity() {
        return mPopularity;
    }

    public void setPopularity(String popularity) {
        this.mPopularity = popularity;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.mPosterUrl = posterUrl;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.mOriginalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.mBackdropPath = backdropPath;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        this.mSynopsis = synopsis;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

}
