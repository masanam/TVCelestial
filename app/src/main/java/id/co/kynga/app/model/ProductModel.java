package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;


public class ProductModel implements Parcelable {
	public static final String TAG = ProductModel.class.getCanonicalName();

	//All parameter must be declared completely, other wise doesn'w work well
	public String id = Config.text_blank;
	public String product_name = Config.text_blank;
	public String product_price = Config.text_blank;

	//Not used parameter
	public long ID;
	public String Title = Config.text_blank;
	public String LinkURL = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public TypeModel Type = TypeModel.NoneType;
	public long Log;

	public ArrayList<ProductModel> list = new ArrayList<>();

	public ProductModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final ProductModel va_model = new ProductModel(array.getJSONObject(counter));
				list.add(va_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public ProductModel(final JSONObject json) {
		try {
			//transaction_response = json.getString("transaction_response");
			id = json.getString("id");
			product_name = json.getString("product_name");
			product_price = json.getString("product_price");

			//Not used
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

	public ProductModel(Parcel in) {
		id = in.readString();
		product_name = in.readString();
		product_price = in.readString();

		//Not used
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
		}
		Log = in.readLong();

		in.readTypedList(list, ProductModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(product_name);
		dest.writeString(product_price);

		//Not used
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
		public ProductModel createFromParcel(Parcel in) {
			return new ProductModel(in);
		}

		@Override
		public ProductModel[] newArray(int size) {
			return new ProductModel[size];
		}
	};
}