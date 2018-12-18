package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;


public class YoutubeModel implements Parcelable {
	public static final String TAG = YoutubeModel.class.getCanonicalName();

	public long ID;
	public String Title = Config.text_blank;
	public String YoutubeID = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public long Log;
	public ArrayList<YoutubeModel> list = new ArrayList<>();

	public YoutubeModel() {
	}

	public YoutubeModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final YoutubeModel youtube_model = new YoutubeModel(array.getJSONObject(counter));
				list.add(youtube_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public YoutubeModel(final JSONObject json) {
		try {
			if (json.has("ID")) {
				ID = json.getLong("ID");
			}
			Title = json.getString("Title");
			YoutubeID = json.getString("YoutubeID");
			ImageURL = json.getString("ImageURL");
			if (json.has("Log")) {
				Log = json.getLong("Log");
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public YoutubeModel(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		YoutubeID = in.readString();
		ImageURL = in.readString();
		Log = in.readLong();
		in.readTypedList(list, YoutubeModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(Title);
		dest.writeString(YoutubeID);
		dest.writeString(ImageURL);
		dest.writeLong(Log);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public YoutubeModel createFromParcel(Parcel in) {
			return new YoutubeModel(in);
		}

		@Override
		public YoutubeModel[] newArray(int size) {
			return new YoutubeModel[size];
		}
	};
}