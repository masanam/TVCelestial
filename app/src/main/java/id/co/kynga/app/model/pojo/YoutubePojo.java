package id.co.kynga.app.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class YoutubePojo implements Parcelable {

    public String PlaylistID;
    public String Title;
    public String ImageURL;

    public YoutubePojo() {
        //empty constructor
    }

    protected YoutubePojo(Parcel in) {
        PlaylistID = in.readString();
        Title = in.readString();
        ImageURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PlaylistID);
        dest.writeString(Title);
        dest.writeString(ImageURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<YoutubePojo> CREATOR = new Creator<YoutubePojo>() {
        @Override
        public YoutubePojo createFromParcel(Parcel in) {
            return new YoutubePojo(in);
        }

        @Override
        public YoutubePojo[] newArray(int size) {
            return new YoutubePojo[size];
        }
    };
}
