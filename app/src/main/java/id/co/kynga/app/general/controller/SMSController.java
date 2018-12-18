package id.co.kynga.app.general.controller;

import android.app.Activity;
import android.content.Intent;

public class SMSController {
	public static void openSMS(
			final Activity activity,
			final String phone_number) {
		String phone = phone_number;
		if (!phone.startsWith("+")) {
			phone = "+" + phone;
		}
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setType("vnd.android-dir/mms-sms");
		intent.putExtra("address", phone);
		intent.putExtra("sms_body", Config.text_blank);
		activity.startActivity(intent);
	}
}