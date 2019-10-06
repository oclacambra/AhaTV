package com.octaltakeoff.ahatv.model;

public class Review {
    private String author;
    private String content;
    private String mReviewId;
    private String url;

    public Review(String author, String content, String reviewId, String url) {
        this.author = author;
        this.content = content;
        this.mReviewId = reviewId;
        this.url = url;
    }

    public Review() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewId() {
        return mReviewId;
    }

    public void setReviewId(String reviewId) {
        this.mReviewId = reviewId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
