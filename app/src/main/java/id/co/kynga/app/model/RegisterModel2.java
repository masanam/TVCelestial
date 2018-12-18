package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class RegisterModel2 {
	public String Token = Config.text_blank;
	public boolean SMSGateway = false;



	public RegisterModel2(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			Token = json.getString("Token");
			SMSGateway = json.getBoolean("SMSGateway");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}