package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;

public class YoutubePlaylistModel implements Parcelable {
	public static final String TAG = YoutubePlaylistModel.class.getCanonicalName();

	public String PlaylistID = Config.text_blank;
	public String Title = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public YoutubeModel Youtube = new YoutubeModel();
	public ArrayList<YoutubePlaylistModel> list = new ArrayList<>();

	public YoutubePlaylistModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final YoutubePlaylistModel youtube_playlist_model = new YoutubePlaylistModel(array.getJSONObject(counter));
				list.add(youtube_playlist_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public YoutubePlaylistModel(final JSONObject json) {
		try {
			PlaylistID = json.getString("PlaylistID");
			Title = json.getString("Title");
			ImageURL = json.getString("ImageURL");
			if (GlobalController.isNotNull(json.getString("Youtube"))) {
				final JSONObject youtube = json.getJSONObject("Youtube");
				Youtube = new YoutubeModel(youtube.getString("List"));
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public YoutubePlaylistModel(Parcel in) {
		PlaylistID = in.readString();
		Title = in.readString();
		ImageURL = in.readString();
		Youtube = in.readParcelable(YoutubeModel.class.getClassLoader());
		in.readTypedList(list, YoutubePlaylistModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(PlaylistID);
		dest.writeString(Title);
		dest.writeString(ImageURL);
		dest.writeParcelable(Youtube, flags);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public YoutubePlaylistModel createFromParcel(Parcel in) {
			return new YoutubePlaylistModel(in);
		}

		@Override
		public YoutubePlaylistModel[] newArray(int size) {
			return new YoutubePlaylistModel[size];
		}
	};
}