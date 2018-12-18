package id.co.kynga.app.ui.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.model.PreferenceEntity;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.PreferenceUtil;
import android.content.Context;
import android.os.StrictMode;

public class RadioAdapter {
	Context ctx;
	String id, code;
	PreferenceUtil pref;
	PreferenceEntity entity;

	public RadioAdapter(Context context, String rId, String rCode) {
		ctx = context;
		id = rId;
		code = rCode;
		pref = new PreferenceUtil();
		entity = new PreferenceEntity(ctx);
		entity.open();
	}

	public ArrayList<HashMap<String, String>> getData() {
		String token = pref.getPreference("token");
		ArrayList<HashMap<String, String>> movieData = new ArrayList<HashMap<String, String>>();
		InputStream is = null;
		String result = "";
		String radio_pref = entity.getPreference(code);//pref.getPreference(code);
		if (radio_pref.length() == 0 || radio_pref.equals("")) {
			try {
				try {
					StrictMode.ThreadPolicy policy = new StrictMode.
							ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					URL url = new URL(DataVariable.BASE_URL + "getRadio/" + id + "?token=" + token);
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.setConnectTimeout(60 * 1000);
					is = con.getInputStream();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}


			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = radio_pref;
		}
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject json_data = jArray.getJSONObject(i);
				map.put("id", json_data.getString("id"));
				map.put(DataVariable.KEY_IMAGE, json_data.getString("img1"));
				map.put(DataVariable.KEY_TITLE, json_data.getString("name"));
				map.put(DataVariable.KEY_URL, json_data.getString("playUrl"));
				map.put(DataVariable.KEY_DESC, json_data.getString("desc"));
				movieData.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return movieData;
	}
}