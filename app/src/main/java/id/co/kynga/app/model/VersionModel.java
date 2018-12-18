package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;


public class VersionModel implements Parcelable {
	public static final String TAG = VersionModel.class.getCanonicalName();

	//All parameter must be declared completely, other wise doesn'w work well
	public long ID;
	public String VersionNumber = Config.text_blank;
	public long Log;

	public ArrayList<VersionModel> list = new ArrayList<>();


	public VersionModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final VersionModel va_model = new VersionModel(array.getJSONObject(counter));
				list.add(va_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VersionModel(final JSONObject json) {
		try {
			VersionNumber = json.getString("VersionNumber");
			ID = json.getLong("ID");
			Log = json.getLong("Log");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VersionModel(Parcel in) {
		VersionNumber = in.readString();
		ID = in.readLong();
		Log = in.readLong();
		in.readTypedList(list, VersionModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(VersionNumber);
		dest.writeLong(ID);
		dest.writeLong(Log);
		dest.writeTypedList(list);

	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public VersionModel createFromParcel(Parcel in) {
			return new VersionModel(in);
		}

		@Override
		public VersionModel[] newArray(int size) {
			return new VersionModel[size];
		}
	};
}