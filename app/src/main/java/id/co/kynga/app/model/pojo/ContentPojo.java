package id.co.kynga.app.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class ContentPojo implements Parcelable {

    public long ID;
    public String RadioCategoryID;
    public String RadioTitle;
    public String Title;
    public String LinkURL;
    public String PlayUrl;
    public String ImageURL;
    public String Package;
    public String DownloadUrl;
    public String Summary;
    public String Desc;
    public String Rating;
    public String Trailer;
    public long Log;

    public ContentPojo() {
        //empty constructor
    }

    private ContentPojo(Parcel in) {
        ID = in.readLong();
        RadioCategoryID = in.readString();
        RadioTitle = in.readString();
        Title = in.readString();
        LinkURL = in.readString();
        PlayUrl = in.readString();
        ImageURL = in.readString();
        Package = in.readString();
        DownloadUrl = in.readString();
        Summary = in.readString();
        Desc = in.readString();
        Rating = in.readString();
        Trailer = in.readString();
        Log = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(RadioCategoryID);
        dest.writeString(RadioTitle);
        dest.writeString(Title);
        dest.writeString(LinkURL);
        dest.writeString(PlayUrl);
        dest.writeString(ImageURL);
        dest.writeString(Package);
        dest.writeString(DownloadUrl);
        dest.writeString(Summary);
        dest.writeString(Desc);
        dest.writeString(Rating);
        dest.writeString(Trailer);
        dest.writeLong(Log);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContentPojo> CREATOR = new Creator<ContentPojo>() {
        @Override
        public ContentPojo createFromParcel(Parcel in) {
            return new ContentPojo(in);
        }

        @Override
        public ContentPojo[] newArray(int size) {
            return new ContentPojo[size];
        }
    };
}
