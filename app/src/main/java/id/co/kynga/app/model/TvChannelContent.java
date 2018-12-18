package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RizkyFadilah on 1/3/17.
 */

@SuppressWarnings("unused, WeakerAccess")
public class TvChannelContent implements Parcelable {
    public static final String TAG = TvChannelContent.class.getCanonicalName();

    public long ID;
    public String Title;
    public String PlayUrl;
    public String Desc;
    public long Log;

    public TvChannelContent() {
    }

    protected TvChannelContent(Parcel in) {
        ID = in.readLong();
        Title = in.readString();
        PlayUrl = in.readString();
        Desc = in.readString();
        Log = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(Title);
        dest.writeString(PlayUrl);
        dest.writeString(Desc);
        dest.writeLong(Log);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TvChannelContent> CREATOR = new Creator<TvChannelContent>() {
        @Override
        public TvChannelContent createFromParcel(Parcel in) {
            return new TvChannelContent(in);
        }

        @Override
        public TvChannelContent[] newArray(int size) {
            return new TvChannelContent[size];
        }
    };
}
