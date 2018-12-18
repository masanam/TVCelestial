package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;


public class YoutubeChannelModel implements Parcelable {
	public long ID;
	public String Title = Config.text_blank;
	public String Summary = Config.text_blank;
	public String ChannelID = Config.text_blank;
	public long Log;
	public ArrayList<YoutubeChannelModel> list = new ArrayList<>();

	public YoutubeChannelModel() {
	}

	public YoutubeChannelModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final YoutubeChannelModel youtube_channel_model = new YoutubeChannelModel(array.getJSONObject(counter));
				list.add(youtube_channel_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public YoutubeChannelModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
			Summary = json.getString("Summary");
			ChannelID = json.getString("ChannelID");
			Log = json.getLong("Log");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public YoutubeChannelModel(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		Summary = in.readString();
		ChannelID = in.readString();
		Log = in.readLong();
		in.readTypedList(list, YoutubeChannelModel.CREATOR);
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
		dest.writeString(ChannelID);
		dest.writeLong(Log);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public YoutubeChannelModel createFromParcel(Parcel in) {
			return new YoutubeChannelModel(in);
		}

		@Override
		public YoutubeChannelModel[] newArray(int size) {
			return new YoutubeChannelModel[size];
		}
	};
}