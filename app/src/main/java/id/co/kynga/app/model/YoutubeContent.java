package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RizkyFadilah on 1/3/17.
 * Modified by Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class YoutubeContent implements Parcelable {

    public static final String TAG = YoutubeContent.class.getSimpleName();
    public String Title;
    public String YoutubeID;
    public String ImageURL;

    public YoutubeContent() {
        //empty constructor
    }

    protected YoutubeContent(Parcel in) {
        Title = in.readString();
        YoutubeID = in.readString();
        ImageURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(YoutubeID);
        dest.writeString(ImageURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<YoutubeContent> CREATOR = new Creator<YoutubeContent>() {
        @Override
        public YoutubeContent createFromParcel(Parcel in) {
            return new YoutubeContent(in);
        }

        @Override
        public YoutubeContent[] newArray(int size) {
            return new YoutubeContent[size];
        }
    };
}
