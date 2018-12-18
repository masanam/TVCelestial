package id.co.kynga.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;
import android.os.StrictMode;
import android.widget.Toast;

public class BannerLoader extends IntentService {
	public static final String URL = "url";
	public static final String PACKAGE = "id.co.kynga.app";
	public static final String BANNER = "banner";
	private PreferenceUtil pref;

	public BannerLoader() {
		super("BannerLoader");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		pref = new PreferenceUtil();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String token = pref.getPreference("token");
		InputStream is = null;
		String urlPath = intent.getStringExtra(URL);
		urlPath += "?token=" + token;
		String result = "";
		//Toast.makeText(getApplicationContext(), urlPath, Toast.LENGTH_LONG).show();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			URL url = new URL(urlPath);
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
			con.setConnectTimeout(60 * 1000);
			is = con.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		loadImage(result);
	}

	private void loadImage(String imags) {
		Intent intent = new Intent(PACKAGE);
		intent.putExtra(BANNER, imags);
		sendBroadcast(intent);
	}
}