package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

/**
 * Created by macbookpro on 10/1/17.
 */

public class BubblePackageModel implements Parcelable {

    public static final String TAG = BubblePackageModel.class.getCanonicalName();

    public long ID;
    public String ImageUrl = Config.text_blank; //(fruit war, bob),( Fish, Cave Escape)
    public String Title = Config.text_blank;
    public String PackageID = Config.text_blank;
    public String PriceID = Config.text_blank;
    public String Price = Config.text_blank;
    public String Period = Config.text_blank;
    public String Remark = Config.text_blank;

    public long Log;
    public ArrayList<BubblePackageModel> list = new ArrayList<>();


    public BubblePackageModel() {
    }

    public BubblePackageModel(final String result) {
        try {
            final JSONArray array = new JSONArray(result);
            for (int counter = 0; counter < array.length(); counter++) {
                final BubblePackageModel game_model = new BubblePackageModel(array.getJSONObject(counter));
                list.add(game_model);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public BubblePackageModel(final JSONObject json) {
        try {
            if (json.has("ID")) {
                ID = json.getLong("ID");
            }

            ImageUrl = json.getString("ImageUrl");
            Title = json.getString("Title");
            PackageID = json.getString("PackageID");
            PriceID = json.getString("PriceID");
            Price = json.getString("Price");
            Period = json.getString("Period");
            Remark = json.getString("Remark");
            if (json.has("Log")) {
                Log = json.getLong("Log");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public BubblePackageModel(Parcel in) {
        ID = in.readLong();
        ImageUrl = in.readString();
        Title = in.readString();
        PackageID = in.readString();
        PriceID = in.readString();
        Price = in.readString();
        Period = in.readString();
        Remark = in.readString();
        Log = in.readLong();
        in.readTypedList(list, BubblePackageModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(ImageUrl);
        dest.writeString(Title);
        dest.writeString(PackageID);
        dest.writeString(PriceID);
        dest.writeString(Price);
        dest.writeString(Period);
        dest.writeString(Remark);
        dest.writeLong(Log);
        dest.writeTypedList(list);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public BubblePackageModel createFromParcel(Parcel in) {
            return new BubblePackageModel(in);
        }

        @Override
        public BubblePackageModel[] newArray(int size) {
            return new BubblePackageModel[size];
        }
    };


}
