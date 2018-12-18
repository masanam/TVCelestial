package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;


public class VideoGroupModel implements Parcelable {
	public static final String TAG = VideoGroupModel.class.getCanonicalName();

	public long ID;
	public String Title = Config.text_blank;
	public ArrayList<VideoGroupModel> list = new ArrayList<>();

	public VideoGroupModel() {
	}

	public VideoGroupModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final VideoGroupModel video_group_model = new VideoGroupModel(array.getJSONObject(counter));
				list.add(video_group_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoGroupModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoGroupModel(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		in.readTypedList(list, VideoGroupModel.CREATOR);
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
		public VideoGroupModel createFromParcel(Parcel in) {
			return new VideoGroupModel(in);
		}

		@Override
		public VideoGroupModel[] newArray(int size) {
			return new VideoGroupModel[size];
		}
	};
}