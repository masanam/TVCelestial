package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class PackageDataPriceModel implements Parcelable {
	public static final String TAG = PackageDataModel.class.getCanonicalName();

	public long ID;
	public long DisplayPrice;

	public PackageDataPriceModel() {
	}

	public PackageDataPriceModel(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			ID = json.getLong("ID");
			DisplayPrice = json.getLong("DisplayPrice");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public PackageDataPriceModel(Parcel in) {
		ID = in.readLong();
		DisplayPrice = in.readLong();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeLong(DisplayPrice);
	}

	public static final Creator CREATOR = new Creator() {
		@Override
		public PackageDataPriceModel createFromParcel(Parcel in) {
			return new PackageDataPriceModel(in);
		}

		@Override
		public PackageDataPriceModel[] newArray(int size) {
			return new PackageDataPriceModel[size];
		}
	};
}