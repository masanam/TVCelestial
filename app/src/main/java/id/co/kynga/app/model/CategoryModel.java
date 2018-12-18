package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class CategoryModel implements Parcelable {
	public static final String TAG = CategoryModel.class.getCanonicalName();

	public long ID;
	public String Title = Config.text_blank;
	public ContentModel Content = new ContentModel();
	public ArrayList<CategoryModel> list = new ArrayList<>();

	public CategoryModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final CategoryModel category_model = new CategoryModel(array.getJSONObject(counter));
				list.add(category_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public CategoryModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
			Content = new ContentModel(json.getString("Content"));
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public CategoryModel(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		Content = in.readParcelable(ContentModel.class.getClassLoader());
		in.readTypedList(list, CategoryModel.CREATOR);
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
		public CategoryModel createFromParcel(Parcel in) {
			return new CategoryModel(in);
		}

		@Override
		public CategoryModel[] newArray(int size) {
			return new CategoryModel[size];
		}
	};
}