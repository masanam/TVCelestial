package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class VideoModel implements Parcelable {
	public static final String TAG = VideoModel.class.getCanonicalName();

	public long ID;
	public String Title = Config.text_blank;
	public String Summary = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public String LinkURL = Config.text_blank;
	public long Log;
	public VideoTrailerModel video_trailer_model = new VideoTrailerModel();
	public ArrayList<VideoModel> list = new ArrayList<>();

	public VideoModel() {
	}

	public VideoModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final VideoModel video_model = new VideoModel(array.getJSONObject(counter));
				list.add(video_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
			Summary = json.getString("Summary");
			ImageURL = json.getString("ImageURL");
			LinkURL = json.getString("LinkURL");
			Log = json.getLong("Log");
			video_trailer_model = new VideoTrailerModel(json.getString("Trailer"));
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VideoModel(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		Summary = in.readString();
		LinkURL = in.readString();
		ImageURL = in.readString();
		Log = in.readLong();
		video_trailer_model = in.readParcelable(VideoTrailerModel.class.getClassLoader());
		in.readTypedList(list, VideoModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(Title);
		dest.writeString(Summary);
		dest.writeString(LinkURL);
		dest.writeString(ImageURL);
		dest.writeLong(Log);
		dest.writeParcelable(video_trailer_model, flags);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public VideoModel createFromParcel(Parcel in) {
			return new VideoModel(in);
		}

		@Override
		public VideoModel[] newArray(int size) {
			return new VideoModel[size];
		}
	};
}