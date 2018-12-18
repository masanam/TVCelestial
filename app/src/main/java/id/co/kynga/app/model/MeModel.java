package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class MeModel {
	public String PhoneNumber = Config.text_blank;
	public String Fullname = Config.text_blank;
	public String Email = Config.text_blank;
	public String VANumber = Config.text_blank;
	public long Credit;

	public MeModel(final String response) {
		try {
			final JSONObject json = new JSONObject(response);
			PhoneNumber = json.getString("PhoneNumber");
			Fullname = json.getString("Fullname");
			Email = json.getString("Email");
			VANumber = json.getString("VANumber");
			Credit = json.getLong("Credit");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}