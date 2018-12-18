package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class RadioModel implements Parcelable {
	public static final String TAG = RadioModel.class.getCanonicalName();

	public long ID;
	public long RadioCategoryID;
	public String Title = Config.text_blank;
	public String Summary = Config.text_blank;
	public String LinkURL = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public long Log;
	public ArrayList<RadioModel> list = new ArrayList<>();

	public RadioModel() {
	}

	public RadioModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final RadioModel radio_model = new RadioModel(array.getJSONObject(counter));
				list.add(radio_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public RadioModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			RadioCategoryID = json.getLong("RadioCategoryID");
			Title = json.getString("Title");
			Summary = json.getString("Summary");
			LinkURL = json.getString("LinkURL");
			ImageURL = json.getString("ImageURL");
			Log = json.getLong("Log");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public RadioModel(Parcel in) {
		ID = in.readLong();
		RadioCategoryID = in.readLong();
		Title = in.readString();
		Summary = in.readString();
		LinkURL = in.readString();
		ImageURL = in.readString();
		Log = in.readLong();
		in.readTypedList(list, RadioModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeLong(RadioCategoryID);
		dest.writeString(Title);
		dest.writeString(Summary);
		dest.writeString(LinkURL);
		dest.writeString(ImageURL);
		dest.writeLong(Log);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public RadioModel createFromParcel(Parcel in) {
			return new RadioModel(in);
		}

		@Override
		public RadioModel[] newArray(int size) {
			return new RadioModel[size];
		}
	};
}