package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class TVCategoryModel implements Parcelable {
	public static final String TAG = TVCategoryModel.class.getCanonicalName();

	public long ID;
	public long TvGroupID;
	public String Title = Config.text_blank;
	public ArrayList<TVCategoryModel> list = new ArrayList<>();


	public TVCategoryModel() {
	}

	public TVCategoryModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final TVCategoryModel tv_category_model = new TVCategoryModel(array.getJSONObject(counter));
				list.add(tv_category_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public TVCategoryModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			TvGroupID = json.getLong("TvGroupID");
			Title = json.getString("Title");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public TVCategoryModel(Parcel in) {
		ID = in.readLong();
		TvGroupID = in.readLong();
		Title = in.readString();
		in.readTypedList(list, TVCategoryModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeLong(TvGroupID);
		dest.writeString(Title);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public TVCategoryModel createFromParcel(Parcel in) {
			return new TVCategoryModel(in);
		}

		@Override
		public TVCategoryModel[] newArray(int size) {
			return new TVCategoryModel[size];
		}
	};
}