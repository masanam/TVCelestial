package id.co.kynga.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.ui.activity.VerificationActivity2;
import id.co.kynga.app.ui.activity.VerificationActivity2AppMStar;

public class SMSReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		final Bundle bundle = intent.getExtras();
		try {
			if (bundle != null) {
				String message;
				String phone_number;
				if (Build.VERSION.SDK_INT >= 19) {
					SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
					final SmsMessage current_message = msgs[0];
					message = current_message.getDisplayMessageBody();
					phone_number = current_message.getDisplayOriginatingAddress();
				} else {
					final Object pdus[] = (Object[]) bundle.get("pdus");
					final SmsMessage current_message = SmsMessage.createFromPdu((byte[]) pdus[0]);
					message = current_message.getDisplayMessageBody();
					phone_number = current_message.getDisplayOriginatingAddress();
				}
				if (GlobalController.isNotNull(message) && GlobalController.isNotNull(phone_number)) {
					//SMS yg diterima dikirimkan ke dua class celestial dan MStar
					VerificationActivity2.checkSMS(message);
					VerificationActivity2AppMStar.checkSMS(message);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}