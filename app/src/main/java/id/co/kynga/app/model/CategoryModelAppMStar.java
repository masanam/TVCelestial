package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class CategoryModelAppMStar implements Parcelable {
	public static final String TAG = CategoryModelAppMStar.class.getCanonicalName();

	public long ID;
	public String Title = Config.text_blank;
	public ContentModelAppMStar Content = new ContentModelAppMStar();
	public ArrayList<CategoryModelAppMStar> list = new ArrayList<>();

	public CategoryModelAppMStar(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final CategoryModelAppMStar category_model = new CategoryModelAppMStar(array.getJSONObject(counter));
				list.add(category_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public CategoryModelAppMStar(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
			Content = new ContentModelAppMStar(json.getString("Content"));
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public CategoryModelAppMStar(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		Content = in.readParcelable(ContentModelAppMStar.class.getClassLoader());
		in.readTypedList(list, CategoryModelAppMStar.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(Title);
		dest.writeParcelable(Content, flags);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public CategoryModelAppMStar createFromParcel(Parcel in) {
			return new CategoryModelAppMStar(in);
		}

		@Override
		public CategoryModelAppMStar[] newArray(int size) {
			return new CategoryModelAppMStar[size];
		}
	};
}