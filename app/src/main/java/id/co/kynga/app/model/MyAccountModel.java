package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class MyAccountModel {
	public long ID = 0;
	public String PhoneNumber = Config.text_blank;
	public String Fullname = Config.text_blank;
	public String Email = Config.text_blank;
	public String Adress = Config.text_blank;
	public String MacAddress = Config.text_blank;
	public String Package = Config.text_blank;
	public long Balance = 0;

	public MyAccountModel(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			PhoneNumber = json.getString("PhoneNumber");
			Fullname = json.getString("Fullname");
			Email = json.getString("Email");
			Adress = json.getString("Adress");
			MacAddress = json.getString("MacAddress");
			Package  = json.getString("Package");
			Balance = json.getLong("Balance");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}