package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class VideoCategoryModel implements Parcelable {
	public static final String TAG = VideoCategoryModel.class.getCanonicalName();

	public long ID;
	public long VideoGroupID;
	public String Title = Config.text_blank;
	public VideoModel Video = new VideoModel();
	public ArrayList<VideoCategoryModel> list = new ArrayList<>();


	public VideoCategoryModel() {
	}

	public VideoCategoryModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final VideoCategoryModel video_category_model = new VideoCategoryModel(array.getJSONObject(counter));
				list.add(video_category_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoCategoryModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			VideoGroupID = json.getLong("VideoGroupID");
			Title = json.getString("Title");
			if (json.has("Video")) {
				Video = new VideoModel(json.getString("Video"));
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoCategoryModel(Parcel in) {
		ID = in.readLong();
		VideoGroupID = in.readLong();
		Title = in.readString();
		Video = in.readParcelable(VideoModel.class.getClassLoader());
		in.readTypedList(list, VideoCategoryModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeLong(VideoGroupID);
		dest.writeString(Title);
		dest.writeParcelable(Video, flags);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public VideoCategoryModel createFromParcel(Parcel in) {
			return new VideoCategoryModel(in);
		}

		@Override
		public VideoCategoryModel[] newArray(int size) {
			return new VideoCategoryModel[size];
		}
	};
}