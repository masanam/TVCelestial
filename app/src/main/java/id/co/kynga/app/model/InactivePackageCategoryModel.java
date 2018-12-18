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

public class InactivePackageCategoryModel implements Parcelable{

    public static final String TAG = InactivePackageCategoryModel.class.getCanonicalName();
    public long ID;
    public String Package;
    public String Type;
    public BubblePackageModel Bubble = new BubblePackageModel();
    public ArrayList<InactivePackageCategoryModel> list = new ArrayList<>();

    public InactivePackageCategoryModel() {
    }

    public InactivePackageCategoryModel(final String result) {
        try {
            final JSONArray array = new JSONArray(result);
            for (int counter = 0; counter < array.length(); counter++) {
                final InactivePackageCategoryModel game_category_model = new InactivePackageCategoryModel(array.getJSONObject(counter));
                list.add(game_category_model);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public InactivePackageCategoryModel(final JSONObject json) {
        try {
            ID = json.getLong("ID");
            Package = json.getString("Package");
            Type = json.getString("Type");

            if (GlobalController.isNotNull(json.getString("Bubble"))) {
                final JSONObject game = json.getJSONObject("Bubble");
                Bubble = new BubblePackageModel(game.getString("List"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public InactivePackageCategoryModel(Parcel in) {
        ID = in.readLong();
        Package = in.readString();
        Type = in.readString();
        Bubble = in.readParcelable(BubblePackageModel.class.getClassLoader());
        in.readTypedList(list, InactivePackageCategoryModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeString(Package);
        dest.writeString(Type);
        dest.writeParcelable(Bubble, flags);
        dest.writeTypedList(list);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public InactivePackageCategoryModel createFromParcel(Parcel in) {
            return new InactivePackageCategoryModel(in);
        }

        @Override
        public InactivePackageCategoryModel[] newArray(int size) {
            return new InactivePackageCategoryModel[size];
        }
    };

}
