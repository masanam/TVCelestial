package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class EWalletModel2 {
	public String user_id = Config.text_blank;
	public String payment_type = Config.text_blank;
	public String order_id = Config.text_blank;
	public String transaction_id = Config.text_blank;
	public String transaction_time = Config.text_blank;
	public String transaction_status = Config.text_blank;
	public String bank = Config.text_blank;
	public String payment_code = Config.text_blank;
	public String gross_amount = Config.text_blank;
	public String redirect_url = Config.text_blank;
	//public long Credit;

	public EWalletModel2(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			user_id = json.getString("user_id");
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
}