package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;


public class ContentModelAppMStar implements Parcelable {
	public long ID;
	public String Title = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public String LinkURL = Config.text_blank;
	public TypeModel Type = TypeModel.NoneType;
	public VideoModel Video = new VideoModel();
	public VideoCategoryModel VideoCategory = new VideoCategoryModel();
	public VideoGroupModel VideoGroup = new VideoGroupModel();
	public TVModel TV = new TVModel();
	public TVCategoryModel TVCategory = new TVCategoryModel();
	public YoutubeModel Youtube = new YoutubeModel();
	public YoutubeChannelModel YoutubeChannel = new YoutubeChannelModel();
	public RadioModel Radio = new RadioModel();
	public RadioCategoryModel RadioCategory = new RadioCategoryModel();
	public GameCategoryModel GameCategory = new GameCategoryModel();
	public GameModel Game = new GameModel();
	public ArrayList<ContentModelAppMStar> list = new ArrayList<>();

	public ContentModelAppMStar() {
	}

	public ContentModelAppMStar(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final ContentModelAppMStar content_model = new ContentModelAppMStar(array.getJSONObject(counter));
				list.add(content_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public ContentModelAppMStar(final JSONObject json) {
		try {
			ID = json.getLong("ID");
			Title = json.getString("Title");
			ImageURL = json.getString("ImageURL");
			LinkURL = json.getString("LinkURL");
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
			} else if (type == TypeModel.RadioType.getValue()) {
				Type = TypeModel.RadioType;
			} else if (type == TypeModel.VAVType.getValue()) {
				Type = TypeModel.VAVType;
			}
			if (GlobalController.isNotNull(json.getString("Video"))) {
				Video = new VideoModel(json.getJSONObject("Video"));
			}
			if (GlobalController.isNotNull(json.getString("VideoCategory"))) {
				VideoCategory = new VideoCategoryModel(json.getJSONObject("VideoCategory"));
			}
			if (GlobalController.isNotNull(json.getString("VideoGroup"))) {
				VideoGroup = new VideoGroupModel(json.getJSONObject("VideoGroup"));
			}
			if (GlobalController.isNotNull(json.getString("TV"))) {
				TV = new TVModel(json.getJSONObject("TV"));
			}
			if (GlobalController.isNotNull(json.getString("TVCategory"))) {
				TVCategory = new TVCategoryModel(json.getJSONObject("TVCategory"));
			}
			if (GlobalController.isNotNull(json.getString("Youtube"))) {
				Youtube = new YoutubeModel(json.getJSONObject("Youtube"));
			}
			if (GlobalController.isNotNull(json.getString("YoutubeChannel"))) {
				YoutubeChannel = new YoutubeChannelModel(json.getJSONObject("YoutubeChannel"));
			}
			if (GlobalController.isNotNull(json.getString("Radio"))) {
				Radio = new RadioModel(json.getJSONObject("Radio"));
			}
			if (GlobalController.isNotNull(json.getString("RadioCategory"))) {
				RadioCategory = new RadioCategoryModel(json.getJSONObject("RadioCategory"));
			}
			if (GlobalController.isNotNull(json.getString("Game"))){
				Game = new GameModel(json.getJSONObject("Game"));
			}
			if (GlobalController.isNotNull(json.getString("GameCategory"))){
				GameCategory = new GameCategoryModel(json.getJSONObject("GameCategory"));
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public ContentModelAppMStar(Parcel in) {
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
		} else if (type == TypeModel.YoutubeChannelType.getValue()) {
			Type = TypeModel.YoutubeChannelType;
		} else if (type == TypeModel.RadioType.getValue()) {
			Type = TypeModel.RadioType;
		}
		Video = in.readParcelable(VideoModel.class.getClassLoader());
		VideoCategory = in.readParcelable(VideoCategoryModel.class.getClassLoader());
		VideoGroup = in.readParcelable(VideoGroupModel.class.getClassLoader());
		TV = in.readParcelable(TVModel.class.getClassLoader());
		TVCategory = in.readParcelable(TVCategoryModel.class.getClassLoader());
		Youtube = in.readParcelable(YoutubeModel.class.getClassLoader());
		YoutubeChannel = in.readParcelable(YoutubeChannelModel.class.getClassLoader());
		Radio = in.readParcelable(RadioCategoryModel.class.getClassLoader());
		RadioCategory = in.readParcelable(RadioCategoryModel.class.getClassLoader());
		Game = in.readParcelable(GameModel.class.getClassLoader());
		GameCategory = in.readParcelable(GameCategoryModel.class.getClassLoader());
		in.readTypedList(list, ContentModelAppMStar.CREATOR);
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
		dest.writeParcelable(Video, flags);
		dest.writeParcelable(VideoCategory, flags);
		dest.writeParcelable(VideoGroup, flags);
		dest.writeParcelable(TV, flags);
		dest.writeParcelable(TVCategory, flags);
		dest.writeParcelable(Youtube, flags);
		dest.writeParcelable(YoutubeChannel, flags);
		dest.writeParcelable(Radio, flags);
		dest.writeParcelable(RadioCategory, flags);
		dest.writeParcelable(Game, flags);
		dest.writeParcelable(GameCategory, flags);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public ContentModelAppMStar createFromParcel(Parcel in) {
			return new ContentModelAppMStar(in);
		}

		@Override
		public ContentModelAppMStar[] newArray(int size) {
			return new ContentModelAppMStar[size];
		}
	};
}