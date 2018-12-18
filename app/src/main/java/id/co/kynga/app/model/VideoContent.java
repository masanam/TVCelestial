package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RizkyFadilah on 1/3/17.
 */

@SuppressWarnings("unused, WeakerAccess")
public class VideoContent implements Parcelable {
    public static final String TAG = VideoContent.class.getSimpleName();
    public long ID;
    public String Title;
    public String Summary;
    public String LinkURL;
    public String Rating;
    public String ImageURL;
    public String Trailer;
    public long Log;

    public VideoContent() {
    }

    protected VideoContent(Parcel in) {
        ID = in.readLong();
        Title = in.readString();
        Summary = in.readString();
        LinkURL = in.readString();
        Rating = in.readString();
        ImageURL = in.readString();
        Trailer = in.readString();
        Log = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(Title);
        dest.writeString(Summary);
        dest.writeString(LinkURL);
        dest.writeString(Rating);
        dest.writeString(ImageURL);
        dest.writeString(Trailer);
        dest.writeLong(Log);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoContent> CREATOR = new Creator<VideoContent>() {
        @Override
        public VideoContent createFromParcel(Parcel in) {
            return new VideoContent(in);
        }

        @Override
        public VideoContent[] newArray(int size) {
            return new VideoContent[size];
        }
    };
}
