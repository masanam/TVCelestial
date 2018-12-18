package id.co.kynga.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.kynga.app.general.controller.Config;

/**
 * Created by macbookpro on 10/1/17.
 */

public class EWalletModel implements Parcelable {

    public static final String TAG = EWalletModel.class.getCanonicalName();

    public String user_id = Config.text_blank; //(fruit war, bob),( Fish, Cave Escape)
    public String payment_type = Config.text_blank;
    public String order_id = Config.text_blank;
    public String transaction_id = Config.text_blank;
    public String transaction_time = Config.text_blank;
    public String transaction_status = Config.text_blank;
    public String bank = Config.text_blank;
    public String payment_code = Config.text_blank;
    public String gross_amount = Config.text_blank;
    public String redirect_url = Config.text_blank;

    public long Log;
    public ArrayList<EWalletModel> list = new ArrayList<>();


    public EWalletModel() {
    }

    public EWalletModel(final String result) {
        try {
            final JSONArray array = new JSONArray(result);
            for (int counter = 0; counter < array.length(); counter++) {
                final EWalletModel game_model = new EWalletModel(array.getJSONObject(counter));
                list.add(game_model);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public EWalletModel(final JSONObject json) {
        try {
            if (json.has("user_id")) {
                user_id = json.getString("user_id");
            }

            payment_type = json.getString("payment_type");
            order_id = json.getString("order_id");
            transaction_id = json.getString("transaction_id");
            transaction_time = json.getString("transaction_time");
            transaction_status = json.getString("transaction_status");
            bank = json.getString("bank");
            payment_code = json.getString("payment_code");
            gross_amount = json.getString("gross_amount");
            redirect_url = json.getString("redirect_url");

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public EWalletModel(Parcel in) {
        user_id = in.readString();
        payment_type = in.readString();
        order_id = in.readString();
        transaction_id = in.readString();
        transaction_time = in.readString();
        transaction_status = in.readString();
        bank = in.readString();
        payment_code = in.readString();
        gross_amount = in.readString();
        redirect_url = in.readString();
        in.readTypedList(list, EWalletModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(payment_type);
        dest.writeString(order_id );
        dest.writeString(transaction_id);
        dest.writeString(transaction_time);
        dest.writeString(transaction_status);
        dest.writeString(bank);
        dest.writeString(payment_code);
        dest.writeString(gross_amount);
        dest.writeString(redirect_url);
        dest.writeTypedList(list);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public EWalletModel createFromParcel(Parcel in) {
            return new EWalletModel(in);
        }

        @Override
        public EWalletModel[] newArray(int size) {
            return new EWalletModel[size];
        }
    };


}
