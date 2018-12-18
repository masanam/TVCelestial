package id.co.kynga.app.general.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;


import static id.co.kynga.app.KyngaApp.context;

public class SMSreceiveController {
	public static boolean isReceiveSmsPermitted() {
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
			return false;
		}

		return true;
	}
}