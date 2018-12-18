package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class LoginModel {
	public String Token = Config.text_blank;
	public String PhoneNumber = Config.text_blank;
	public String Fullname = Config.text_blank;
	public String Email = Config.text_blank;
	public long Credit;

	public LoginModel(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			Token = json.getString("Token");
			PhoneNumber = json.getString("PhoneNumber");
			Fullname = json.getString("Fullname");
			Email = json.getString("Email");
			Credit = json.getLong("Credit");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}