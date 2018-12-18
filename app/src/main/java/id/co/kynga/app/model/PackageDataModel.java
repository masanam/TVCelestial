package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;


public class PackageDataModel implements Parcelable {
	public static final String TAG = PackageDataModel.class.getCanonicalName();

	public long ID;
	public long MobileOperatorID;
	public String Name = Config.text_blank;
	public String ImageURL = Config.text_blank;
	public String PackageID = Config.text_blank;
	public String Description = Config.text_blank;
	public String Price = Config.text_blank;
	public PackageDataPriceModel package_data_price_model = new PackageDataPriceModel();
	public ArrayList<PackageDataModel> list = new ArrayList<>();

	public PackageDataModel(final String result) {
		try {
			final JSONArray array = new JSONArray(result);
			for (int counter = 0; counter < array.length(); counter++) {
				final PackageDataModel package_data_model = new PackageDataModel(array.getJSONObject(counter));
				list.add(package_data_model);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public PackageDataModel(final JSONObject json) {
		try {
			ID = json.getLong("id");
			MobileOperatorID = json.getLong("mobile_operator_id");
			Name = json.getString("name");
			ImageURL = json.getString("image_url");
			PackageID = json.getString("package_id");
			Description = json.getString("description");
			Price = json.getString("package_price");
			//package_data_price_model = new PackageDataPriceModel(json.getString("package_price"));
			/*
			ID = json.getLong("ID");
			MobileOperatorID = json.getLong("MobileOperatorID");
			Name = json.getString("Name");
			ImageURL = json.getString("ImageURL");
			PackageID = json.getString("PackageID");
			Description = json.getString("Description");
			package_data_price_model = new PackageDataPriceModel(json.getString("Price"));
			*/
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public PackageDataModel(Parcel in) {
		ID = in.readLong();
		MobileOperatorID = in.readLong();
		Name = in.readString();
		ImageURL = in.readString();
		PackageID = in.readString();
		Description = in.readString();
		Price = in.readString();
		package_data_price_model = in.readParcelable(PackageDataPriceModel.class.getClassLoader());
		in.readTypedList(list, PackageDataModel.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeLong(MobileOperatorID);
		dest.writeString(Name);
		dest.writeString(ImageURL);
		dest.writeString(PackageID);
		dest.writeString(Description);
		dest.writeString(Price);
		dest.writeParcelable(package_data_price_model, flags);
		dest.writeTypedList(list);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public PackageDataModel createFromParcel(Parcel in) {
			return new PackageDataModel(in);
		}

		@Override
		public PackageDataModel[] newArray(int size) {
			return new PackageDataModel[size];
		}
	};
}