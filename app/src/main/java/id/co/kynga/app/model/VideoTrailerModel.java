package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class VideoTrailerModel implements Parcelable {
	public static final String TAG = VideoTrailerModel.class.getCanonicalName();

	public long ID;
	public String LinkURL = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public ArrayList<VideoTrailerModel> list = new ArrayList<>();

	public VideoTrailerModel() {
	}

	public VideoTrailerModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final VideoTrailerModel video_trailer_model = new VideoTrailerModel(array.getJSONObject(counter));
				list.add(video_trailer_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoTrailerModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			LinkURL = json.getString("LinkURL");
			ImageURL = json.getString("ImageURL");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoTrailerModel(Parcel in) {
		ID = in.readLong();
		LinkURL = in.readString();
		ImageURL = in.readString();
		in.readTypedList(list, VideoTrailerModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(LinkURL);
		dest.writeString(ImageURL);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public VideoTrailerModel createFromParcel(Parcel in) {
			return new VideoTrailerModel(in);
		}

		@Override
		public VideoTrailerModel[] newArray(int size) {
			return new VideoTrailerModel[size];
		}
	};
}