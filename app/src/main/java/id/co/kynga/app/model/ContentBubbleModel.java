package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by RizkyFadilah on 28/2/17.
 */

@SuppressWarnings("unused, WeakerAccess")
public class ContentBubbleModel implements Parcelable {
    public long Status;
    public String Message;
    public List<Content> Result;

    public ContentBubbleModel() {
    }

    public static class Content implements Parcelable {
        public long ID;
        public String Title;
        public String Category;
        public List<TvChannelContent> TvChannel;
        public List<VideoContent> Video;
        public List<RadioContent> Radio;
        public List<YoutubeContent> Youtube;


        protected Content(Parcel in) {
            ID = in.readLong();
            Title = in.readString();
            Category = in.readString();
            TvChannel = in.createTypedArrayList(TvChannelContent.CREATOR);
            Video = in.createTypedArrayList(VideoContent.CREATOR);
            Radio = in.createTypedArrayList(RadioContent.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(ID);
            dest.writeString(Title);
            dest.writeString(Category);
            dest.writeTypedList(TvChannel);
            dest.writeTypedList(Video);
            dest.writeTypedList(Radio);
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

    protected ContentBubbleModel(Parcel in) {
        Status = in.readLong();
        Message = in.readString();
        Result = in.createTypedArrayList(Content.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Status);
        dest.writeString(Message);
        dest.writeTypedList(Result);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContentBubbleModel> CREATOR = new Creator<ContentBubbleModel>() {
        @Override
        public ContentBubbleModel createFromParcel(Parcel in) {
            return new ContentBubbleModel(in);
        }

        @Override
        public ContentBubbleModel[] newArray(int size) {
            return new ContentBubbleModel[size];
        }
    };
}
