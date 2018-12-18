package id.co.kynga.app.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * CategoryPojo
 *
 * @author Aji Subastian
 */

@SuppressWarnings("unused, WeakerAccess")
public class CategoryPojo implements Parcelable {

    public int Status;
    public String Message;
    public List<Content> Result;

    public CategoryPojo() {
        //Empty constructor
    }

    protected CategoryPojo(Parcel in) {
        Status = in.readInt();
        Message = in.readString();
        Result = in.createTypedArrayList(Content.CREATOR);
    }

    public static final Creator<CategoryPojo> CREATOR = new Creator<CategoryPojo>() {
        @Override
        public CategoryPojo createFromParcel(Parcel in) {
            return new CategoryPojo(in);
        }

        @Override
        public CategoryPojo[] newArray(int size) {
            return new CategoryPojo[size];
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

    @Override
    public String toString() {
        return "CategoryPojo{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", Result=" + Result +
                '}';
    }

    public static class Content implements Parcelable {

        public long ID;
        public String Title;
        public String ImageURL;
        public String Type;

        public Content() {
            //Empty constructor
        }

        protected Content(Parcel in) {
            ID = in.readLong();
            Title = in.readString();
            ImageURL = in.readString();
            Type = in.readString();
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
            dest.writeString(Type);
        }

        @Override
        public String toString() {
            return "Content{" +
                    "ID=" + ID +
                    ", Title='" + Title + '\'' +
                    ", ImageURL='" + ImageURL + '\'' +
                    ", Type=" + Type +
                    '}';
        }
    }

}
