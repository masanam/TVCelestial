package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.GlobalController;

/**
 * Created by macbookpro on 10/1/17.
 */

public class BubblePackageCategoryModel implements Parcelable{

    public static final String TAG = BubblePackageCategoryModel.class.getCanonicalName();
    public long ID;
    public String Package;
    public String fullname;
    public BubblePackageModel Bubble = new BubblePackageModel();
    public ArrayList<BubblePackageCategoryModel> list = new ArrayList<>();

    public BubblePackageCategoryModel() {
    }

    public BubblePackageCategoryModel(final String result) {
        try {
            final JSONArray array = new JSONArray(result);
            for (int counter = 0; counter < array.length(); counter++) {
                final BubblePackageCategoryModel game_category_model = new BubblePackageCategoryModel(array.getJSONObject(counter));
                list.add(game_category_model);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public BubblePackageCategoryModel(final JSONObject json) {
        try {
            ID = json.getLong("ID");
            Package = json.getString("Package");
            fullname = json.getString("fullname");

            if (GlobalController.isNotNull(json.getString("Bubble"))) {
                final JSONObject game = json.getJSONObject("Bubble");
                Bubble = new BubblePackageModel(game.getString("List"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public BubblePackageCategoryModel(Parcel in) {
        ID = in.readLong();
        Package = in.readString();
        fullname = in.readString();
        Bubble = in.readParcelable(BubblePackageModel.class.getClassLoader());
        in.readTypedList(list, BubblePackageCategoryModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(Package);
        dest.writeString(fullname);
        dest.writeParcelable(Bubble, flags);
        dest.writeTypedList(list);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public BubblePackageCategoryModel createFromParcel(Parcel in) {
            return new BubblePackageCategoryModel(in);
        }

        @Override
        public BubblePackageCategoryModel[] newArray(int size) {
            return new BubblePackageCategoryModel[size];
        }
    };

}
