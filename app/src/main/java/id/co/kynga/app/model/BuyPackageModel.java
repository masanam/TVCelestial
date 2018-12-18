package id.co.kynga.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.general.controller.Config;


public class BuyPackageModel {
	public String transactionStatus = Config.text_blank;
	public String transactionMessage = Config.text_blank;


	public BuyPackageModel(final String result) {
		try {
			final JSONObject json = new JSONObject(result);
			transactionStatus = json.getString("transactionStatus");
			transactionMessage = json.getString("transactionMessage");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
}