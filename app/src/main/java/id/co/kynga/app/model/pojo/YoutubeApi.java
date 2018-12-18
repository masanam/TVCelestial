package id.co.kynga.app.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import id.co.kynga.app.model.YoutubeContent;

/**
 * @author Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class YoutubeApi implements Parcelable {

    public int Status;
    public String Message;
    public Content Result;

    public YoutubeApi() {
        //empty constructor
    }

    protected YoutubeApi(Parcel in) {
        Status = in.readInt();
        Message = in.readString();
        Result = in.readParcelable(Content.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Status);
        dest.writeString(Message);
        dest.writeParcelable(Result, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<YoutubeApi> CREATOR = new Creator<YoutubeApi>() {
        @Override
        public YoutubeApi createFromParcel(Parcel in) {
            return new YoutubeApi(in);
        }

        @Override
        public YoutubeApi[] newArray(int size) {
            return new YoutubeApi[size];
        }
    };

    public static class Content implements Parcelable {

        public long Total;
        public String nextToken;
        public String prevToken;
        public List<YoutubeContent> List;

        public Content() {
            //empty constructor
        }

        private Content(Parcel in) {
            Total = in.readLong();
            nextToken = in.readString();
            prevToken = in.readString();
            List = in.createTypedArrayList(YoutubeContent.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(Total);
            dest.writeString(nextToken);
            dest.writeString(prevToken);
            dest.writeTypedList(List);
        }

        @Override
        public int describeContents() {
            return 0;
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
    }

}
