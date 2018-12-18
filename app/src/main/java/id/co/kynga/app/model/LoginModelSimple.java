package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class LoginModelSimple {
	public String PhoneNumber = Config.text_blank;
	public LoginModelSimple(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			PhoneNumber = json.getString("PhoneNumber");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}