package id.co.kynga.app.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * BannerPojo
 *
 * @author Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class BannerPojo implements Parcelable {

    public int Status;
    public String Message;
    public List<Content> Result;

    public BannerPojo() {
        //Empty constructor
    }

    protected BannerPojo(Parcel in) {
        Status = in.readInt();
        Message = in.readString();
        Result = in.createTypedArrayList(Content.CREATOR);
    }

    public static final Creator<BannerPojo> CREATOR = new Creator<BannerPojo>() {
        @Override
        public BannerPojo createFromParcel(Parcel in) {
            return new BannerPojo(in);
        }

        @Override
        public BannerPojo[] newArray(int size) {
            return new BannerPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Status);
        dest.writeString(Message);
        dest.writeTypedList(Result);
    }

    public static class Content implements Parcelable {

        public Long ID;
        public String Title;
        public String ImageURL;
        public Long Log;

        public Content() {
            //Empty constructor
        }

        protected Content(Parcel in) {
            ID = in.readLong();
            Title = in.readString();
            ImageURL = in.readString();
            Log = in.readLong();
        }

        public static final Creator<Content> CREATOR = new Creator<Content>() {
            @Override
            public Content createFromParcel(Parcel in) {
                return new Content(in);
            }

            @Override
            public Content[] newArray(int size) {
                return new Content[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(ID);
            dest.writeString(Title);
            dest.writeString(ImageURL);
            dest.writeLong(Log);
        }
    }

}
