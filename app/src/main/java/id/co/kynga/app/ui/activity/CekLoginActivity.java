package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;


public class CekLoginActivity extends Activity {
	public static CekLoginActivity instance;

	private VideoView vw_video;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_cek_login);
		//request permission for marsmallow up

		if (!RequestActivity.isAllPermitted()) {
			AppController.openRequestActivity(instance);
			finish();
		}else
		{
			checkLogin();
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
		//setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}
/*
	private void setInitial() {
		//vw_video = (VideoView) findViewById(R.id.vw_video);
		//img_bg = (RecyclingImageView) findViewById(R.id.img_bg);
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
						//img_bg.setVisibility(View.GONE);
					}
				});
			}
		});
		vw_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {

				//Check status login IMEI ke API server
				checkLogin();
				//AppController.checkSession(instance, true);
			}
		});
	}
*/
	private void checkLogin() {
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = telephonyManager.getDeviceId();
		//GlobalController.showLoading(this);

		URLController.checkLogin(IMEI, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						//setValidation(response, phone);
						//GlobalController.showToast(response, 20000);


						if (response.contains("Imei Is Login")){
							//tujuannya agar bisa langsung pakai app
							AppController.checkSession(instance, true);
						}
/*
						if (response.contains("Imei Not Found")){
							SessionController.close();
							final Intent intent = new Intent(CekLoginActivity.this, LoginActivity.class);
							startActivity(intent);
						}
						if (response.contains("Please Login")){
							SessionController.close();
							final Intent intent = new Intent(CekLoginActivity.this, LoginActivity.class);
							startActivity(intent);
						}
						finish();
*/

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
/*

	private void setValidation(final String response, final String phone_number) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//txt_phone_number.setText(Config.text_blank);
			try {
				final JSONObject json = new JSONObject(response_model.Result);
				if (json.getBoolean("SMSGateway")) {
					SessionController.setPhoneNumberVer(phone_number);
					AppController.openVerificationActivity(this, phone_number);
					finish();
				} else {
					//authenticateNumber(phone_number);
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}

*/
}