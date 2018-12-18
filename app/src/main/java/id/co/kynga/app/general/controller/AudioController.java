package id.co.kynga.app.general.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import id.co.kynga.app.KyngaApp;

public class AudioController {
	public static boolean isRecordAudioPermitted() {
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(KyngaApp.getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			return false;
		}

		return true;
	}
}