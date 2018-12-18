package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import id.co.kynga.app.R;
import id.co.kynga.app.control.SpinnerCustomOnItemSelectedListener;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.AccountCCModel;
import id.co.kynga.app.model.RegisterModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.UserModel;


public class RegisterActivity_cc extends Activity {
	public static RegisterActivity_cc instance;

	private ImageButton btnBack, btnScan;
	private EditText textFullname, textCC, textMM, textYY, textCVV ;
	private ImageButton btnAdd, btnEdit;
	private String phone_number;
	private TextView textRespon, textTitle;
	private Spinner spinner1;
	DatePicker picker;
	private ImageButton displayDate, imgBtnDate;
	private LinearLayout linearLayoutDatePicker, layoutBlank;
	private static Typeface tf_roboto;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_register_cc);
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		textFullname = (EditText) findViewById(R.id.textFullname);
		textCC = (EditText) findViewById(R.id.textCC);
		textMM = (EditText) findViewById(R.id.textMM);
		textYY = (EditText) findViewById(R.id.textYY);
		textCVV = (EditText) findViewById(R.id.textCVV);
		btnAdd = (ImageButton) findViewById(R.id.btnAdd);
		btnEdit = (ImageButton) findViewById(R.id.btnEdit);
		layoutBlank = (LinearLayout)findViewById(R.id.layoutBlank);
		setEventListener();
		textCC.addTextChangedListener(new FourDigitCardFormatWatcher());
		checkAccountCC();

	}

	private void setEventListener() {

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doBack();
			}
		});

		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doSubmit();
			}
		});
		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppController.openRegisterActivity_cc_Edit(instance);
				finish();
			}
		});

		textCC.requestFocus();
		textCC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				textCC.requestFocus();
				textCC.getText().clear();
			}
		});

	}

	private void doBack() {
		//AppController.openLoginActivity(this);
		finish();
		this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}

	private void doSubmit() {
		final String fullname = textFullname.getText().toString();
		final String ccNumber = textCC.getText().toString().replaceAll("-", "");
		final String mm = textMM.getText().toString();
		final String yy = textYY.getText().toString();
		final String cvv = textCVV.getText().toString();
/*
		if (!GlobalController.isNotNull(fullname)) {
			GlobalController.showWarning(this, R.string.error_fullname_blank);
			return;
		} else if (!GlobalController.isEmail(email)) {
			GlobalController.showWarning(this, R.string.error_email);
			return;
		} else if (!GlobalController.isNotNull(gender)) {
			GlobalController.showWarning(this, R.string.error_gender);
			return;
		}
*/
		if (!GlobalController.isNotNull(fullname)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textFullname.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(ccNumber)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textCC.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(mm)) {
			//GlobalController.showWarning(this, R.string.error_cc_exp_month_blank);
			textMM.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(yy)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textMM.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(cvv)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textCVV.setError("mandatory");
			return;
		}
		//TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		//String IMEI = telephonyManager.getDeviceId();
		//final String imei = telephonyManager.getDeviceId();
		//final UserModel user_model = SessionController.getUser();
		//final String phoneNumber = user_model.PhoneNumber;
		final String  phoneNumber = SessionController.getPhoneNumberVer();

		GlobalController.showLoading(this);
		URLController.registerCC(phoneNumber, fullname, ccNumber, mm,yy, cvv, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						//GlobalController.showToast(response, 20000);
						setValidation(response);
						//textRespon.setText(response);
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

	private void setValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType && response.contains("successfully")) {
			GlobalController.showSuccessMessage(instance);
			btnAdd.setVisibility(View.GONE);
			btnEdit.setVisibility(View.VISIBLE);
			layoutBlank.setVisibility(View.GONE);

		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}

	private void checkAccountCC() {
		//final UserModel user_model = SessionController.getUser();
		//final String phoneNumber = user_model.PhoneNumber;
		final String  phoneNumber = SessionController.getPhoneNumberVer();

		GlobalController.showLoading(this);
		URLController.accountCC(phoneNumber, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						//GlobalController.showToast(response, 20000);
						setValidationCheckAccountCC(response);
						//textRespon.setText(response);
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

	private void setValidationCheckAccountCC(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final AccountCCModel accountCC_model = new AccountCCModel(response_model.Result);
			//SessionController.open(register_model);
			//AppController.checkSession(this, true);
			//GlobalController.showToast(accountCC_model.Fullname, 20000);
			textFullname.setText(accountCC_model.Fullname);
			//textCC.setText(accountCC_model.number);
			/*
			textCC.setText(accountCC_model.number.substring(0,4)+"-"+
					accountCC_model.number.substring(4,8)+"-"+
			accountCC_model.number.substring(8,12)+"-"+
					accountCC_model.number.substring(12,16));
			*/
			textCC.setText("**** **** **** "+
					accountCC_model.number.substring(12,16));
			textMM.setText(accountCC_model.expired_month);
			textYY.setText(accountCC_model.expired_year);
			textCVV.setText(accountCC_model.cwcv);

			textCVV.setVisibility(View.GONE);
			btnAdd.setVisibility(View.GONE);
			btnEdit.setVisibility(View.VISIBLE);
			layoutBlank.setVisibility(View.GONE);
			textFullname.setEnabled(false);
			textCC.setEnabled(false);
			textMM.setEnabled(false);
			textYY.setEnabled(false);
			textCVV.setEnabled(false);
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			//GlobalController.showWarning(this, response_model.Message);
			btnAdd.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.GONE);
			layoutBlank.setVisibility(View.GONE);
		}
	}

	public static class FourDigitCardFormatWatcher implements TextWatcher {

		// Change this to what you want... ' ', '-' etc..
		private static final char space = '-';

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Remove spacing char
			if (s.length() > 0 && (s.length() % 5) == 0) {
				final char c = s.charAt(s.length() - 1);
				if (space == c) {
					s.delete(s.length() - 1, s.length());
				}
			}
			// Insert char where needed.
			if (s.length() > 0 && (s.length() % 5) == 0) {
				char c = s.charAt(s.length() - 1);
				// Only if its a digit where there should be a space we insert a space
				if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
					s.insert(s.length() - 1, String.valueOf(space));
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}
}