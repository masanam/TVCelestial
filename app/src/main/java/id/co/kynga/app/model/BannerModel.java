package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;


public class BannerModel implements Parcelable {
	public static final String TAG = BannerModel.class.getCanonicalName();

	public long ID;
	public String Title = Config.text_blank;
	public String LinkURL = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public TypeModel Type = TypeModel.NoneType;
	public long Log;
	public ArrayList<BannerModel> list = new ArrayList<>();

	public BannerModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final BannerModel banner_model = new BannerModel(array.getJSONObject(counter));
				list.add(banner_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public BannerModel(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
			LinkURL = json.getString("LinkURL");
			ImageURL = json.getString("ImageURL");
			final int type = json.getInt("Type");
			if (type == TypeModel.NoneType.getValue()) {
				Type = TypeModel.NoneType;
			} else if (type == TypeModel.WebType.getValue()) {
				Type = TypeModel.WebType;
			} else if (type == TypeModel.VideoType.getValue()) {
				Type = TypeModel.VideoType;
			} else if (type == TypeModel.TVType.getValue()) {
				Type = TypeModel.TVType;
			} else if (type == TypeModel.YoutubeType.getValue()) {
				Type = TypeModel.YoutubeType;
			} else if (type == TypeModel.YoutubeChannelType.getValue()) {
				Type = TypeModel.YoutubeChannelType;
			}
			Log = json.getLong("Log");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public BannerModel(Parcel in) {
		ID = in.readLong();
		Title = in.readString();
		LinkURL = in.readString();
		ImageURL = in.readString();
		final int type = in.readInt();
		if (type == TypeModel.NoneType.getValue()) {
			Type = TypeModel.NoneType;
		} else if (type == TypeModel.WebType.getValue()) {
			Type = TypeModel.WebType;
		} else if (type == TypeModel.VideoType.getValue()) {
			Type = TypeModel.VideoType;
		} else if (type == TypeModel.TVType.getValue()) {
			Type = TypeModel.TVType;
		} else if (type == TypeModel.YoutubeType.getValue()) {
			Type = TypeModel.YoutubeType;
		} else if (type ==TypeModel.YoutubeChannelType.getValue()) {
			Type = TypeModel.YoutubeChannelType;
		}
		Log = in.readLong();
		in.readTypedList(list, BannerModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(Title);
		dest.writeString(LinkURL);
		dest.writeString(ImageURL);
		dest.writeInt(Type.getValue());
		dest.writeLong(Log);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public BannerModel createFromParcel(Parcel in) {
			return new BannerModel(in);
		}

		@Override
		public BannerModel[] newArray(int size) {
			return new BannerModel[size];
		}
	};
}