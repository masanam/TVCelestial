package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class TVModel implements Parcelable {
	public static final String TAG = TVModel.class.getCanonicalName();

	public long ID;
	public long TVCategoryID;
	public String Title = Config.text_blank;
	public String Summary = Config.text_blank;
	public String LinkURL = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public long Log;
	public ArrayList<TVModel> list = new ArrayList<>();

	public TVModel() {
	}

	public TVModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final TVModel tv_model = new TVModel(array.getJSONObject(counter));
				list.add(tv_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public TVModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			TVCategoryID = json.getLong("TVCategoryID");
			Title = json.getString("Title");
			Summary = json.getString("Summary");
			LinkURL = json.getString("LinkURL");
			ImageURL = json.getString("ImageURL");
			Log = json.getLong("Log");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public TVModel(Parcel in) {
		ID = in.readLong();
		TVCategoryID = in.readLong();
		Title = in.readString();
		Summary = in.readString();
		LinkURL = in.readString();
		ImageURL = in.readString();
		Log = in.readLong();
		in.readTypedList(list, TVModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeLong(TVCategoryID);
		dest.writeString(Title);
		dest.writeString(Summary);
		dest.writeString(LinkURL);
		dest.writeString(ImageURL);
		dest.writeLong(Log);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public TVModel createFromParcel(Parcel in) {
			return new TVModel(in);
		}

		@Override
		public TVModel[] newArray(int size) {
			return new TVModel[size];
		}
	};
}