package id.co.kynga.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.R;
import id.co.kynga.app.control.RecyclingImageView;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.VersionModel;

import static id.co.kynga.app.KyngaApp.context;

public class SplashActivityAppMStar extends CommonActivity {
	public static SplashActivityAppMStar instance;

	private VideoView vw_video;
	private RecyclingImageView img_bg;

	private SharedPreferences sharedpreferences;
	public static final String mypreference = "mypref";
	private String appVersion, currentVersion="0";

	private VersionModel version_model;
	private static int versionNumber;
	private static String loginResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_splash);
		//request permission for marsmallow up
		if (!RequestActivity.isAllPermitted()) {
			AppController.openRequestActivity(instance);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		vw_video = (VideoView) findViewById(R.id.vw_video);
		img_bg = (RecyclingImageView) findViewById(R.id.img_bg);
		final String path = "android.resource://" + getPackageName() + "/" + R.raw.splash;
		final Uri uri = Uri.parse(path);
		vw_video.setVideoURI(uri);
		vw_video.requestFocus();
		vw_video.start();
		vw_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						img_bg.setVisibility(View.GONE);
					}
				});
			}
		});
		vw_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				checkLogin();
			}
		});
	}

	private void checkLogin() {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String IMEI = telephonyManager.getDeviceId();
		GlobalController.showLoading(this);
		URLController.checkLoginAppMStar(IMEI, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						loginResult = response;
						//setValidation(response, phone);
						//GlobalController.showToast(response, 20000);
						if (response.contains("Imei Is Login")){
							//tujuannya agar bisa langsung pakai app
							AppController.checkSessionAppMStar(instance, true);
						}
						if (response.contains("Imei Not Found")){
							SessionControllerAppMStar.close();
							final Intent intent = new Intent(context, LoginActivity_AppMStar.class);
							startActivity(intent);
						}
						if (response.contains("Please Login")){
							SessionControllerAppMStar.close();
							final Intent intent = new Intent(context, LoginActivity_AppMStar.class);
							startActivity(intent);
						}
						finish();
					}
				});
			}

			@Override
			public void didURLFailed() {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						GlobalController.showRequestFailedWarning(instance);
					}
				});
			}
		});
	}

}