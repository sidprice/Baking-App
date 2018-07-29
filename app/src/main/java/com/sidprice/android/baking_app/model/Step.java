package com.sidprice.android.baking_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private int     id ;
    private String  short_description ;
    private String  description ;
    private String  video_url ;
    private String  thumbnail_url ;

    public Step() {

    }
    protected Step(Parcel in) {
        id = in.readInt();
        short_description = in.readString();
        description = in.readString();
        video_url = in.readString();
        thumbnail_url = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(short_description);
        dest.writeString(description);
        dest.writeString(video_url);
        dest.writeString(thumbnail_url);
    }
}
