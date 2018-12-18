package id.co.kynga.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.json.JSONArray;

import id.co.kynga.app.model.Preference;
import id.co.kynga.app.model.PreferenceEntity;
import android.app.IntentService;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

public class LoaderService extends IntentService {
	public static final String DATA = "data";
	private PreferenceUtil pref;
	private PreferenceEntity entity;

	public LoaderService() {
		super("LoaderService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ArrayList<String> movieData = intent.getExtras().getStringArrayList(DATA);
		if (movieData.size() > 0) {
			for (String code : movieData) {
				try {
					StringTokenizer paramCode = new StringTokenizer(code, "|");
					String prefCode = paramCode.nextToken();
					String prefUrl = paramCode.nextToken();
					String prefData = getData(prefUrl);
					try {
						JSONArray data = new JSONArray(prefData);
						if (data.length() > 0) {
							storePreference(prefCode, prefData);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		pref = new PreferenceUtil();
		entity = new PreferenceEntity(this);
		entity.open();
	}

	private String getData(String uri) {
		String token = pref.getPreference(DataVariable.TOKEN);
		uri += "?token=" + token;
		String result = "";
		InputStream is = null;
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
			con.setConnectTimeout(6000);
			is = con.getInputStream();
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
			Log.d("buffer", "Error converting result " + e.toString());
		}

		return result;

	}

	private void broadcastData(String arraydata) {
		if (arraydata != null) {
			Intent intent = new Intent(DataVariable.PACKAGE);
			intent.putExtra(DATA, arraydata);
			sendBroadcast(intent);
		}
	}

	private void storePreference(String code, String p) {
		Preference pr = new Preference();
		pr.setName(code);
		pr.setValue(p);
		boolean isStored = entity.isDataSored(code);
		if (isStored) {
			entity.update(pr);
		} else {
			entity.insert(pr);
		}
	}
}