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

public class GameCategoryModel implements Parcelable{

    public static final String TAG = GameCategoryModel.class.getCanonicalName();
    public long catID;
    public String catTitle;//Action [], Racing[]
    public GameModel Games = new GameModel();
    public ArrayList<GameCategoryModel> list = new ArrayList<>();

    public GameCategoryModel() {
    }

    public GameCategoryModel(final String result) {
        try {
            final JSONArray array = new JSONArray(result);
            for (int counter = 0; counter < array.length(); counter++) {
                final GameCategoryModel game_category_model = new GameCategoryModel(array.getJSONObject(counter));
                list.add(game_category_model);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public GameCategoryModel(final JSONObject json) {
        try {
            catID = json.getLong("catID");
            catTitle = json.getString("catTitle");

            if (GlobalController.isNotNull(json.getString("Games"))) {
                final JSONObject game = json.getJSONObject("Games");
                Games = new GameModel(game.getString("List"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public GameCategoryModel(Parcel in) {
        catID = in.readLong();
        catTitle = in.readString();
        Games = in.readParcelable(GameModel.class.getClassLoader());
        in.readTypedList(list, GameCategoryModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(catID);
        dest.writeString(catTitle);
        dest.writeParcelable(Games, flags);
        dest.writeTypedList(list);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public GameCategoryModel createFromParcel(Parcel in) {
            return new GameCategoryModel(in);
        }

        @Override
        public GameCategoryModel[] newArray(int size) {
            return new GameCategoryModel[size];
        }
    };

}
