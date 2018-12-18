package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RadioCategoryModel implements Parcelable {
	public long ID;
	public String Title;
	public ArrayList<RadioCategoryModel> list = new ArrayList<>();

	public RadioCategoryModel() {
	}

	public RadioCategoryModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final RadioCategoryModel radio_category_model = new RadioCategoryModel(array.getJSONObject(counter));
				list.add(radio_category_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public RadioCategoryModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public RadioCategoryModel(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		in.readTypedList(list, RadioCategoryModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(Title);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public RadioCategoryModel createFromParcel(Parcel in) {
			return new RadioCategoryModel(in);
		}

		@Override
		public RadioCategoryModel[] newArray(int size) {
			return new RadioCategoryModel[size];
		}
	};
}