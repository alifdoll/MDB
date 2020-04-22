package com.alif.submission.mdb.ui.show.item;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_shows")
public class ShowItem implements Parcelable {

    @PrimaryKey
    private int id;
    private String Title;
    private String Overview;
    private String PosterPath;
    @Ignore
    private boolean favorite;

    public ShowItem(){

    }

    protected ShowItem(Parcel in) {
        id = in.readInt();
        Title = in.readString();
        Overview = in.readString();
        PosterPath = in.readString();
    }

    public static final Creator<ShowItem> CREATOR = new Creator<ShowItem>() {
        @Override
        public ShowItem createFromParcel(Parcel in) {
            return new ShowItem(in);
        }

        @Override
        public ShowItem[] newArray(int size) {
            return new ShowItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        this.Overview = overview;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.PosterPath = posterPath;
    }

    public boolean isFavorite(){return favorite;}

    public void setFavorite(boolean favorite){this.favorite = favorite;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Title);
        dest.writeString(Overview);
        dest.writeString(PosterPath);
    }
}
