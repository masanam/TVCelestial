package id.co.kynga.app.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class SubCategory implements Parcelable {

    public int Status;
    public String Message;
    public List<Content> Result;

    public SubCategory() {
        //empty constructor
    }

    private SubCategory(Parcel in) {
        Status = in.readInt();
        Message = in.readString();
        Result = in.createTypedArrayList(Content.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Status);
        dest.writeString(Message);
        dest.writeTypedList(Result);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };

    public static class Content implements Parcelable {

        public long ID;
        public String Category;
        public String Title;
        public String ImageURL;
        public List<ContentPojo> TvChannel;
        public List<ContentPojo> Video;
        public List<ContentPojo> Radio;
        public List<YoutubePojo> Youtube;
        public List<ContentPojo> Games;

        public Content() {
            //empty constructor
        }

        private Content(Parcel in) {
            ID = in.readLong();
            Category = in.readString();
            Title = in.readString();
            ImageURL = in.readString();
            TvChannel = in.createTypedArrayList(ContentPojo.CREATOR);
            Video = in.createTypedArrayList(ContentPojo.CREATOR);
            Radio = in.createTypedArrayList(ContentPojo.CREATOR);
            Youtube = in.createTypedArrayList(YoutubePojo.CREATOR);
            Games = in.createTypedArrayList(ContentPojo.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(ID);
            dest.writeString(Category);
            dest.writeString(Title);
            dest.writeString(ImageURL);
            dest.writeTypedList(TvChannel);
            dest.writeTypedList(Video);
            dest.writeTypedList(Radio);
            dest.writeTypedList(Youtube);
            dest.writeTypedList(Games);
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

        @Override
        public String toString() {
            return "Content{" +
                    "ID=" + ID +
                    ", Category='" + Category + '\'' +
                    ", Title='" + Title + '\'' +
                    ", ImageURL='" + ImageURL + '\'' +
                    ", TvChannel=" + TvChannel +
                    ", Video=" + Video +
                    ", Radio=" + Radio +
                    ", Youtube=" + Youtube +
                    ", Games=" + Games +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", Result=" + Result +
                '}';
    }
}
