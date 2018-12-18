package id.co.kynga.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.R;
import id.co.kynga.app.control.PrefixEditText;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.ResponseModel;

import static id.co.kynga.app.KyngaApp.context;


public class LoginActivity extends CommonActivity {
	public static LoginActivity instance;

	private PrefixEditText txt_phone_number;
	private ImageButton btn_login;
	private CountryCodePicker ccp;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_login);
		setInitial();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		txt_phone_number = (PrefixEditText) findViewById(R.id.txt_phone_number);
		btn_login = (ImageButton) findViewById(R.id.btn_login);
		ccp = (CountryCodePicker) findViewById(R.id.ccp);
		setEventListener();
		//populateData();
	}

	private void setEventListener() {
		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doLogin();
			}
		});
	}

	private void populateData() {
		txt_phone_number.setPrefix(Config.default_phone_prefix);
		txt_phone_number.setPrefixTextColor(Color.YELLOW);
	}

	private void doLogin() {
		String phone_number = txt_phone_number.getText().toString();
		if (!GlobalController.isNotNull(phone_number)) {
			GlobalController.showWarning(this, R.string.error_phone_number_blank);
			return;
		}
		//phone_number = Config.default_phone_prefix + phone_number; selalu +62
		phone_number = ccp.getSelectedCountryCodeWithPlus() + phone_number;
		final String phone = phone_number;
		GlobalController.showLoading(this);
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String IMEI = telephonyManager.getDeviceId();
		//URLController.login(phone_number, new URLManager.URLCallback() {
		URLController.login2(IMEI,phone_number, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						setValidation(response, phone);
						//GlobalController.showToast(response, 20000);
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

	private void setValidation(final String response, final String phone_number) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			txt_phone_number.setText(Config.text_blank);
			try {
				final JSONObject json = new JSONObject(response_model.Result);
				if (json.getBoolean("SMSGateway")) {
					SessionController.setPhoneNumberVer(phone_number);
					AppController.openVerificationActivity(this, phone_number);
					finish();
				} else {
					authenticateNumber(phone_number);
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}

	private void authenticateNumber(final String phone_number) {
		final AuthCallback auth_callback = new AuthCallback() {
			@Override
			public void success(DigitsSession session, String phoneNumber) {
				Digits.clearActiveSession();
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AppController.openRegisterActivity(instance, phone_number);
						finish();
					}
				});
			}

			@Override
			public void failure(DigitsException exception) {
			}
		};
		final AuthConfig.Builder auth_config = new AuthConfig.Builder()
				.withAuthCallBack(auth_callback)
				.withPhoneNumber(phone_number);
				//Log.d(phone_number, phone_number);
				//.withPhoneNumber("+62" + phone_number);
				//.withPhoneNumber(ccp.getSelectedCountryCodeWithPlus() + phone_number);
		Digits.authenticate(auth_config.build());
	}
}