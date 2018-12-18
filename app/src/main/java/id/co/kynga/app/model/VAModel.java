package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

public class VAModel implements Parcelable {
	public static final String TAG = VAModel.class.getCanonicalName();

	//All parameter must be declared completely, other wise doesn'w work well
	public String va_number = Config.text_blank;
	public String payment_id = Config.text_blank;
	public String transaction_time = Config.text_blank;
	public String transaction_id = Config.text_blank;
	public String gross_amount = Config.text_blank;
	public String payment_type = Config.text_blank;
	public String payment_code = Config.text_blank;
	public String bank = Config.text_blank;
	public String payment_status = Config.text_blank;
	public String error_message = Config.text_blank;


	//Not used parameter
	public long ID;
	public String Title = Config.text_blank;
	public String LinkURL = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public TypeModel Type = TypeModel.NoneType;
	public long Log;

	public ArrayList<VAModel> list = new ArrayList<>();


	public VAModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final VAModel va_model = new VAModel(array.getJSONObject(counter));
				list.add(va_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public VAModel(final JSONObject json) {
		try {
			//transaction_response = json.getString("transaction_response");
			va_number = json.getString("va_number");
			payment_id = json.getString("payment_id");
			transaction_time = json.getString("transaction_time");
			transaction_id = json.getString("transaction_id");
			gross_amount = json.getString("gross_amount");
			payment_type = json.getString("payment_type");
			payment_code = json.getString("payment_code");
			bank = json.getString("bank");
			payment_status = json.getString("payment_status");
			error_message = json.getString("error_message");


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

	public VAModel(Parcel in) {
		va_number = in.readString();
		payment_id = in.readString();
		transaction_time = in.readString();
		transaction_id = in.readString();
		gross_amount = in.readString();
		payment_type = in.readString();
		payment_code= in.readString();
		bank = in.readString();
		payment_status = in.readString();
		error_message = in.readString();


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

		in.readTypedList(list, VAModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(va_number);
		dest.writeString(payment_id);
		dest.writeString(transaction_time);
		dest.writeString(transaction_id);
		dest.writeString(gross_amount);
		dest.writeString(payment_type);
		dest.writeString(payment_code);
		dest.writeString(bank);
		dest.writeString(payment_status);
		dest.writeString(error_message);


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
		public VAModel createFromParcel(Parcel in) {
			return new VAModel(in);
		}

		@Override
		public VAModel[] newArray(int size) {
			return new VAModel[size];
		}
	};
}