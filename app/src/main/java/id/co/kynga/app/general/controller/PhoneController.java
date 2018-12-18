package id.co.kynga.app.general.controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import static id.co.kynga.app.KyngaApp.context;


public class PhoneController {
	public static boolean isCallPermitted() {
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			return false;
		}
		return true;
	}

	public static String getDeviceID() {
		try {
			final TelephonyManager telephony_manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephony_manager.getDeviceId() != null) {
				return telephony_manager.getDeviceId();
			} else {
				return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			}
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
		return Config.text_blank;
	}
}