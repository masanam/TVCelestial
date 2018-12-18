package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RizkyFadilah on 1/3/17.
 */

@SuppressWarnings("unused, WeakerAccess")
public class RadioContent implements Parcelable {
    public static final String TAG = RadioContent.class.getCanonicalName();
    public long ID;
    public String RadioCategoryID;
    public String Summary;
    public String RadioTitle;
    public String LinkURL;
    public String ImageURL;
    public long Log;

    public RadioContent() {
    }

    protected RadioContent(Parcel in) {
        ID = in.readLong();
        RadioCategoryID = in.readString();
        Summary = in.readString();
        RadioTitle = in.readString();
        LinkURL = in.readString();
        ImageURL = in.readString();
        Log = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(RadioCategoryID);
        dest.writeString(Summary);
        dest.writeString(RadioTitle);
        dest.writeString(LinkURL);
        dest.writeString(ImageURL);
        dest.writeLong(Log);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RadioContent> CREATOR = new Creator<RadioContent>() {
        @Override
        public RadioContent createFromParcel(Parcel in) {
            return new RadioContent(in);
        }

        @Override
        public RadioContent[] newArray(int size) {
            return new RadioContent[size];
        }
    };
}