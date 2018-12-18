package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class AccountCCModel {
	public String ID = Config.text_blank;
	public String user_id = Config.text_blank;
	public String Fullname = Config.text_blank;
	public String number = Config.text_blank;
	public String expired_month = Config.text_blank;
	public String expired_year = Config.text_blank;
	public String cwcv = Config.text_blank;
	//public long Credit;

	public AccountCCModel(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			ID = json.getString("ID");
			user_id = json.getString("user_id");
			Fullname = json.getString("Fullname");
			number = json.getString("number");
			expired_month = json.getString("expired_month");
			expired_year = json.getString("expired_year");
			cwcv = json.getString("cwcv");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}