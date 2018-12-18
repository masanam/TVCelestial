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
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class TvAdapter {

	//static final String INTL_TV = "intlTv";
	//static final String INDIAN_TV = "indianTv";
	//static final String MANDARIN_TV = "mandarinTv";
	Context ctx;
	String id, code;
	PreferenceUtil pref;
	PreferenceEntity prefEntity;

	public TvAdapter(Context context, String tId, String tCode) {
		ctx = context;
		id = tId;
		code = tCode;

		pref = new PreferenceUtil();
		prefEntity = new PreferenceEntity(ctx);
		prefEntity.open();
	}

	@SuppressLint("NewApi")
	public ArrayList<HashMap<String, String>> getData() {
		String token = pref.getPreference("token");
		ArrayList<HashMap<String, String>> movieData = new ArrayList<HashMap<String, String>>();
		InputStream is = null;

		String result = "";
		String TvData = prefEntity.getPreference(code);// pref.getPreference(code);
		if (TvData.length() == 0 || TvData.equals("")) {


			try {

				try {
					StrictMode.ThreadPolicy policy = new StrictMode.
							ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					URL url = new URL(DataVariable.BASE_URL + "tv/" + id + "?token=" + token);
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.setConnectTimeout(60 * 1000);
					is = con.getInputStream();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.d("http_error", "Error in http connection " + e.toString());
				Toast.makeText(ctx.getApplicationContext(), "Connection error, please check your internet connection", Toast.LENGTH_LONG).show();
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
				//Log.d("result",result);
				//pref.setPreference(code, result);
			} catch (Exception e) {
				Log.d("convertResult", "Error converting result " + e.toString());

			}

		} else {

			result = TvData;

		}

		try {

			//	if(result !=""){


			JSONArray jArray = new JSONArray(result);

			// if(jArray.length() >0){


			for (int i = 0; i < jArray.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject json_data = jArray.getJSONObject(i);
				map.put("id", json_data.getString("id"));
				map.put(DataVariable.KEY_IMAGE, json_data.getString("img1"));
				map.put(DataVariable.KEY_TITLE, json_data.getString("name"));
				map.put(DataVariable.KEY_URL, json_data.getString("playUrl"));
				map.put(DataVariable.KEY_DESC, json_data.getString("desc"));
				//movieData.add(map);
				movieData.add(map);
				//Log.d("data",map.toString());
			}
			// }
			//}

		} catch (JSONException e) {
			Log.d("parsing_data", "Error parsing data " + e.toString());

		}
		return movieData;
	}
}