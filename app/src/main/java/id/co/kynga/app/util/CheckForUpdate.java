package id.co.kynga.app.util;

import android.app.IntentService;
import android.content.Intent;

public class CheckForUpdate extends IntentService {
	public static final String URL = "url";
	public static final String UPDATE = "update";
	public static final String UPDATE_AVAILABLE = "updateIsAvailable";
	private PreferenceUtil pref;

	public CheckForUpdate() {
		super("CheckForUpdate");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		pref = new PreferenceUtil();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String url = intent.getStringExtra(URL);
		String token = pref.getPreference("token");
		url += "?token=" + token;
		UpdateChecker checker = new UpdateChecker(getApplicationContext(), false);
		checker.checkForUpdateByVersionCode(url);
		Boolean isUpdateAvailable = checker.isUpdateAvailable();
		if (isUpdateAvailable) {
			sendBroadcast();
		}
	}

	private void sendBroadcast() {
		Intent intent = new Intent(DataVariable.PACKAGE);
		intent.putExtra(UPDATE, UPDATE_AVAILABLE);
		sendBroadcast(intent);
	}

	private long value(String string) {
		string = string.trim();
		if (string.contains(".")) {
			final int index = string.lastIndexOf(".");
			return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
		} else {
			return Long.valueOf(string);
		}
	}
}
