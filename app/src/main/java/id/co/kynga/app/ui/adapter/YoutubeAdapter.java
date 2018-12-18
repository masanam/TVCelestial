package id.co.kynga.app.ui.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.model.PreferenceEntity;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.PreferenceUtil;
import android.content.Context;
import android.os.StrictMode;

public class YoutubeAdapter {
	Context ctx;
	PreferenceUtil pref;
	String prefCode, playlistUrl;
	PreferenceEntity entity;

	public YoutubeAdapter(Context context, String pCode, String pUrl) {
		ctx = context;
		prefCode = pCode;
		playlistUrl = pUrl;
		pref = new PreferenceUtil();
		entity = new PreferenceEntity(ctx);
		entity.open();
	}

	public ArrayList<Map<String, String>> getData() {
		String token = pref.getPreference("token");
		playlistUrl += "?token=" + token;
		ArrayList<Map<String, String>> movieData = new ArrayList<>();
		InputStream is = null;
		String result = "";
		String playlist_pref = entity.getPreference(prefCode);
		if (playlist_pref.length() == 0 || playlist_pref.equals("")) {
			try {
				try {
					StrictMode.ThreadPolicy policy = new StrictMode.
							ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					URL url = new URL(DataVariable.BASE_URL + playlistUrl);
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
			result = playlist_pref;
		}
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject json_data = jArray.getJSONObject(i);
				map.put(DataVariable.YOUTUBE_KEY_ID, json_data.getString("id"));
				map.put(DataVariable.YOUTUBE_KEY_TITLE, json_data.getString("title"));
				JSONObject thumbnail = json_data.getJSONObject("thumbnail");
				map.put(DataVariable.YOUTUBE_KEY_IMAGE, thumbnail.getString("hqDefault"));
				map.put(DataVariable.YOUTUBE_KEY_AUTHOR, json_data.getString("author"));
				movieData.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return movieData;
	}
}