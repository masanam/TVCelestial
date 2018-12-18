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

public class CategoriesAdapter {
	Context context;
	PreferenceUtil pref;
	String categoriesCode, urlparams;
	private PreferenceEntity prefEntity;

	public CategoriesAdapter(Context c, String code, String urlCode) {

		context = c;
		categoriesCode = code;
		urlparams = urlCode;
		pref = new PreferenceUtil();
		prefEntity = new PreferenceEntity(context);
		prefEntity.open();

	}

	@SuppressLint("NewApi")
	public ArrayList<HashMap<String, String>> getData() {
		//Toast.makeText(context,"test",Toast.LENGTH_LONG).show();
		String token = pref.getPreference("token");
		urlparams += "?token=" + token;
		ArrayList<HashMap<String, String>> movieData = new ArrayList<HashMap<String, String>>();
		InputStream is = null;

		String result = "";
		String pref_movie = prefEntity.getPreference(categoriesCode);//pref.getPreference(categoriesCode);
		//Toast.makeText(context.getApplicationContext(), String.valueOf(pref_movie), Toast.LENGTH_LONG).show();
		if (pref_movie.length() == 0 || pref_movie.equals("")) {
			try {
				//Toast.makeText(context.getApplicationContext(), "test Url", Toast.LENGTH_LONG).show();
				try {
					StrictMode.ThreadPolicy policy = new StrictMode.
							ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					//URL url = new URL("http://www.xlnonton.com/api_client/getAndroidcategoryChannel.php");
					URL url = new URL(DataVariable.BASE_URL + urlparams);
					//Toast.makeText(context.getApplicationContext(), url.toString(), Toast.LENGTH_LONG).show();

					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.setConnectTimeout(60 * 1000);

					is = con.getInputStream();
					Log.d("RequestUrl", url.toString());
				} catch (Exception e) {
					e.printStackTrace();
					//Toast.makeText(context,"Connection error, please check your internet connection",Toast.LENGTH_LONG).show();

				}

			} catch (Exception e) {
				Log.d("http_error", "Error in http connection " + e.toString());

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
				//Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
				//Example of result: id = 150, name = 4K GALERY
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = pref_movie;

		}

		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject json_data = jArray.getJSONObject(i);
				map.put("id", json_data.getString("id"));
				map.put("name", json_data.getString("name"));
				movieData.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return movieData;
	}
}