package com.octaltakeoff.ahatv.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Channel implements Parcelable {

    private int id;
    private String title;
    private String region;
    private String category;
    private String imageUrl;
    private String videoUrl;

    /**
     * Create the default Constructor
     */

    public Channel() {
    }

    public Channel(int id, String title, String region, String category, String imageUrl, String videoUrl) {
        this.id = id;
        this.title = title;
        this.region = region;
        this.category = category;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    protected Channel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        region = in.readString();
        category = in.readString();
        imageUrl = in.readString();
        videoUrl = in.readString();
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(region);
        dest.writeString(category);
        dest.writeString(imageUrl);
        dest.writeString(videoUrl);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", region='" + region + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
