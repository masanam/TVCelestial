package id.co.kynga.app.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.AudioController;
import id.co.kynga.app.general.controller.FileController;
import id.co.kynga.app.general.controller.PhoneController;
import id.co.kynga.app.general.controller.SMSreceiveController;

public class RequestActivity extends FragmentActivity {
	private Button btn_check;
	public RequestActivity instance;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_request);
		instance = this;
		setInitial();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (isAllPermitted()) {
			AppController.openChooseBoxOrMobileActivity(instance);
			finish();
		}
	}

	public static boolean isAllPermitted() {
		boolean valid = true;
		if (!FileController.isStoragePermitted()) {
			valid = false;
		}
		if (!AudioController.isRecordAudioPermitted()) {
			valid = false;
		}
		if (!SMSreceiveController.isReceiveSmsPermitted()) {
			valid = false;
		}
		if (!PhoneController.isCallPermitted()) {
			valid = false;
		}

		return valid;
	}

	private void setInitial() {
		btn_check = (Button) findViewById(R.id.btn_check);
		setEventListener();
	}

	private void setEventListener() {
		btn_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isAllPermitted()) {
					doEnable();
				}else{
					AppController.openChooseBoxOrMobileActivity(instance);
					finish();
					//instance.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
				}
			}
		});
	}

	private void doEnable() {
		if (!isAllPermitted()) {
			ActivityCompat.requestPermissions(this, new String[]{
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.CAMERA,
					Manifest.permission.CALL_PHONE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.RECORD_AUDIO,
					Manifest.permission.READ_PHONE_STATE,
					Manifest.permission.RECEIVE_SMS
			}, 101);
		}
	}

	private void checkAllPermission() {
		if (RequestActivity.isAllPermitted()) {
			//AppController.openFirstActivity0_new(instance);
			AppController.openChooseBoxOrMobileActivity(instance);
			finish();
			//this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
		} else {
			AlertDialog.Builder b = new AlertDialog.Builder(RequestActivity.this);
			b.setTitle("Request");
			b.setMessage("Please enable all permission");
			b.setPositiveButton("OK", new Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dial, int arg1) {
					dial.dismiss();
				}

			});
			b.create().show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			checkAllPermission();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}