package id.co.kynga.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import id.co.kynga.app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.model.Preference;
import id.co.kynga.app.model.PreferenceEntity;
import android.app.IntentService;
import android.content.Intent;

public class DownloadService extends IntentService {
	public static final String PREF_KEY = "preference_key";
	public static final String RESULT = "result";
	public static final String URL = "url";
	private PreferenceUtil pref;
	private PreferenceEntity entity;

	private ArrayList<String[]> prefData = new ArrayList<>();

	public DownloadService() {
		super("DownloadService");
		pref = new PreferenceUtil();

		prefData.add(new String[]{DataVariable.MOVIE_CATEGORIES, "movieCategories"});
		prefData.add(new String[]{DataVariable.FREE_CATEGORIES, "tvCategories/M-INTERNA"});
		prefData.add(new String[]{DataVariable.PREMIUM_CATEGORIES, "tvCategories/PREMIUM-TV"});
		prefData.add(new String[]{DataVariable.GENFLIX_CATEGORIES, "tvCategories/GENFLIX"});
		prefData.add(new String[]{DataVariable.MIVO_CATEGORIES, "tvCategories/MIVO"});
		prefData.add(new String[]{DataVariable.INTERNET_CATEGORIES, "tvCategories/WWWTV"});
		prefData.add(new String[]{DataVariable.MUSIC, "youtubeMusic"});
		prefData.add(new String[]{DataVariable.INFOTAINTMENT, "youtubeInfo"});
		prefData.add(new String[]{DataVariable.LIFESTYLE, "youtubeLifestyle"});
		prefData.add(new String[]{DataVariable.RADIO_CATEGORIES, "getCategoryRadio"});
		prefData.add(new String[]{DataVariable.VIVA_CATEGORIES, "tvCategories/VIVA2"});
		prefData.add(new String[]{DataVariable.CHINESE_CATEGORIES, "tvCategories/CHINESE"});
		prefData.add(new String[]{DataVariable.KOREA_CATEGORIES, "tvCategories/KOREAN"});
		prefData.add(new String[]{DataVariable.INDIAN_CATEGORIES, "tvCategories/INDIA"});
		prefData.add(new String[]{DataVariable.RUSIAN_CATEGORIES, "tvCategories/RUSIAN"});
		prefData.add(new String[]{DataVariable.HINDIE_CATEGORIES, "indiaCategories"});
		prefData.add(new String[]{DataVariable.KIDS_CATEGORIES, "youtubeKids"});
		prefData.add(new String[]{DataVariable.K_CATEGORIES, "tvCategories/4K"});
		prefData.add(new String[]{DataVariable.HAPPEN_CATEGORIES, "tvCategories/HAPPEN"});
		prefData.add(new String[]{DataVariable.HAPPEN_VOD_CATEGORIES, "happenCategories"});
		prefData.add(new String[]{DataVariable.N3_CATEGORIES, "tvCategories/n3"});
		prefData.add(new String[]{DataVariable.YOUTUBE_LIVE_TV_CATEGORIES, "youtubeLiveTv"});
		prefData.add(new String[]{DataVariable.YOUTUBE_4K, "youtube4K"});
		prefData.add(new String[]{DataVariable.FOUR_K_CATEGORIES, "fourKCategories"});
	}

	@Override
	public void onCreate() {
		super.onCreate();
		pref = new PreferenceUtil();
		entity = new PreferenceEntity(this);
		entity.open();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		for (String[] d : prefData) {
			String pUrl = DataVariable.BASE_URL + d[1];
			String rCode = d[0];
			sendRequest(rCode, pUrl);
		}
	}

	private void sendRequest(final String requestCode, String requestUrl) {
		final String token = pref.getPreference(DataVariable.TOKEN);
		requestUrl += "?token=" + token;
		StringRequest request = new StringRequest(Request.Method.GET,
				requestUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						try {
							JSONArray result = new JSONArray(arg0);
							if (result.length() > 0) {
								storePreference(requestCode, arg0);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				arg0.printStackTrace();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<>();
				headers.put("Cookie", "App=" + DataVariable.Md5(token + getResources().getString(R.string.app_name)));
				return headers;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(request);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		prefData.clear();
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