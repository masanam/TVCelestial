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

public class GameModel implements Parcelable {

    public static final String TAG = GameModel.class.getCanonicalName();

    public long gamesID;
    public String gamesTitle = Config.text_blank; //(fruit war, bob),( Fish, Cave Escape)
    public String gamesThumb = Config.text_blank;
    public String gamesLink = Config.text_blank;
    public long Log;
    public ArrayList<GameModel> list = new ArrayList<>();


    public GameModel() {
    }

    public GameModel(final String result) {
        try {
            final JSONArray array = new JSONArray(result);
            for (int counter = 0; counter < array.length(); counter++) {
                final GameModel game_model = new GameModel(array.getJSONObject(counter));
                list.add(game_model);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public GameModel(final JSONObject json) {
        try {
            if (json.has("gamesID")) {
                gamesID = json.getLong("gamesID");
            }

            gamesTitle = json.getString("gamesTitle");
            gamesThumb = json.getString("gamesThumb");
            gamesLink = json.getString("gamesLink");
            if (json.has("Log")) {
                Log = json.getLong("Log");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public GameModel(Parcel in) {
        gamesID = in.readLong();
        gamesTitle = in.readString();
        gamesThumb = in.readString();
        gamesLink = in.readString();
        Log = in.readLong();
        in.readTypedList(list, GameModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(gamesID);
        dest.writeString(gamesTitle);
        dest.writeString(gamesThumb);
        dest.writeString(gamesLink);
        dest.writeLong(Log);
        dest.writeTypedList(list);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public GameModel createFromParcel(Parcel in) {
            return new GameModel(in);
        }

        @Override
        public GameModel[] newArray(int size) {
            return new GameModel[size];
        }
    };


}
